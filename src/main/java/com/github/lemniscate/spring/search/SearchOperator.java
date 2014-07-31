package com.github.lemniscate.spring.search;

import com.github.lemniscate.spring.search.spec.*;
import com.github.lemniscate.spring.search.spec.el.ElLikeSpecification;
import com.github.lemniscate.spring.search.spec.el.ElMatchSpecification;
import com.google.common.collect.Lists;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
* @Author dave 6/29/14 5:18 PM
*/
public enum SearchOperator {
    MATCH, LIKE, MIN, MAX, NOT,
    EL_MATCH, EL_LIKE, EL_MIN, EL_MAX, EL_NOT,

    OR, AND;

    public <T> Specification<T> getSpecification(String[] params, Object payload, OperationParser<T> parser, ConversionService conversionService) {
        switch(this){

            case AND:
            case OR:

                Assert.isAssignable(Collection.class, payload.getClass(), "Must be a collection here");
                // probably should make sure our collection contains maps

                List<Specification> specs = Lists.newArrayList();
                for(Map<String, Object> map : (Collection<Map<String, Object>>) payload){
                    Specification<?> s = parser.parse(map);
                    specs.add(s);
                }
                AndOrSpecification<T> spec = new AndOrSpecification<T>(this, specs);
                return spec;

            //region Direct Properties
            case MATCH:
                return new MatchSpecification<T>(params[0], payload, conversionService);

            case LIKE:
                return new LikeSpecification<T>(params[0], payload, conversionService);

            case MIN:
                return new MinSpecification<T>(params[0], payload, conversionService);

            case MAX:
                return new MaxSpecification<T>(params[0], payload, conversionService);

            case NOT:
                return new NotSpecification<T>(params[0], payload, conversionService);

            //endregion

            //region Collection Properties
            case EL_MATCH:
                return new ElMatchSpecification<T>(params[0], params[1], payload, conversionService);

            case EL_LIKE:
                return new ElLikeSpecification<T>(params[0], params[1], payload, conversionService);

            //endregion
        }

        throw new IllegalStateException("Unhandled operation: " + this);
    }
}
