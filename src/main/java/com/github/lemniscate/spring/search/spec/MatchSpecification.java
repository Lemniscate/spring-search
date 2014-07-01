package com.github.lemniscate.spring.search.spec;

import org.springframework.core.convert.ConversionService;

import javax.persistence.criteria.*;

/**
* @Author dave 6/29/14 5:14 PM
*/
public class MatchSpecification<E> extends BaseSpecification<E> {

    public MatchSpecification(String key, Object value, ConversionService conversionService) {
        super(key, value, conversionService);
    }

    @Override
    public Predicate toPredicate(Root<E> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Path<?> prop = resolveProperty(root, key);
        Object typedValue = conversionService.convert( value, prop.getJavaType() );
        return cb.equal(prop, typedValue);
    }

}
