package com.github.lemniscate.spring.search.spec;

import org.springframework.core.convert.ConversionService;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.From;
import javax.persistence.criteria.Path;

public abstract class BaseSpecification<E> implements Specification<E> {

    protected final String key;
    protected final Object value;
    protected final ConversionService conversionService;

    protected BaseSpecification(String key, Object value, ConversionService conversionService) {
        this.key = key;
        this.value = value;
        this.conversionService = conversionService;
    }

    public Path<?> resolveProperty(Path<?> prop, String key) {
        String[] keys = key.split("\\.");
        for (String k : keys) {
            prop = prop.get(k);
        }
        return prop;
    }

    public From resolveJoin(From from, String key) {
        String[] keys = key.split("\\.");
        for (String k : keys) {
            from = from.join(k);
        }
        return from;
    }

}