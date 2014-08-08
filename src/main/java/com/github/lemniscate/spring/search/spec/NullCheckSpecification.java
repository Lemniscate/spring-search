package com.github.lemniscate.spring.search.spec;

import org.springframework.core.convert.ConversionService;

import javax.persistence.criteria.*;

/**
* @Author dave 6/29/14 5:14 PM
*/
public class NullCheckSpecification<E> extends BaseSpecification<E> {

    private final boolean isNull;

    public NullCheckSpecification(String key, boolean isNull, ConversionService conversionService) {
        super(key, null, conversionService);
        this.isNull = isNull;
    }

    @Override
    public Predicate toPredicate(Root<E> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Path<?> prop = resolveProperty(root, key);
        return isNull ? cb.isNull(prop) : cb.isNotNull(prop);
    }

}
