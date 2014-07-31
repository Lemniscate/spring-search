package com.github.lemniscate.spring.search.spec.el;

import com.github.lemniscate.spring.search.spec.BaseSpecification;
import org.springframework.core.convert.ConversionService;

import javax.persistence.criteria.*;

/**
 * @Author dave 6/29/14 5:21 PM
 */
public class ElMatchSpecification<T> extends BaseSpecification<T> {

    private final String elProperty;

    public ElMatchSpecification(String key, String elProperty, Object payload, ConversionService conversionService) {
        super(key, payload, conversionService);
        this.elProperty = elProperty;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        From join = resolveJoin(root, key);
        Path<?> path = resolveProperty(join, elProperty);
        return cb.equal( path, value);
    }
}
