package com.github.lemniscate.spring.search.spec;

import org.springframework.core.convert.ConversionService;
import org.springframework.util.Assert;

import javax.persistence.criteria.*;

/**
* @Author dave 6/29/14 5:12 PM
*/
public class LikeSpecification<E> extends BaseSpecification<E> {

    public LikeSpecification(String key, Object value, ConversionService conversionService) {
        super(key, value, conversionService);
        Assert.isInstanceOf(String.class, value, "Can only LIKE on Strings");
    }

    @Override
    public Predicate toPredicate(Root<E> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Path<?> prop = resolveProperty(root, key);
        return cb.like((Path<String>) prop, value.toString());
    }

    @Override
    public String toString() {
        return String.format("LIKE(%s - %s)", key, value);
    }

}
