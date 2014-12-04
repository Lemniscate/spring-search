package com.github.lemniscate.spring.search.spec;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.Assert;

import javax.persistence.criteria.*;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class BetweenSpecification<E> extends BaseSpecification<E> {

    public BetweenSpecification(String key, Object value, ConversionService conversionService) {
        super(key, DateRange.rangeFrom(value, conversionService), conversionService);
    }

    @Override
    public Predicate toPredicate(Root<E> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Path<?> prop = resolveProperty(root, key);
        DateRange range = (DateRange) this.value;
        return cb.between((Path<Date>) prop, range.getLower(), range.getUpper());
    }

    @Override
    public String toString() {
        return String.format("Between(%s - %s)", key, value);
    }

    @Getter
    @RequiredArgsConstructor
    private static class DateRange{
        private final Date lower, upper;

        protected static DateRange rangeFrom(Object value, ConversionService service){
            Assert.notNull(value, "Must supply a non-null List value");
            Assert.isTrue( List.class.isAssignableFrom(value.getClass()), "Value should be a list");
            List<?> val = (List<?>) value;
            Date l = service.convert(val.get(0), Date.class);
            Date u = service.convert(val.get(1), Date.class);
            return new DateRange(l, u);
        }
    }

}
