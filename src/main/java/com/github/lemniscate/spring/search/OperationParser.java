package com.github.lemniscate.spring.search;

import com.github.lemniscate.spring.search.spec.AndOrSpecification;
import com.github.lemniscate.spring.search.spec.ElMatchSpecification;
import com.github.lemniscate.spring.search.spec.LikeSpecification;
import com.github.lemniscate.spring.search.spec.MatchSpecification;
import com.google.common.base.CaseFormat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Map;

/**
 * @Author dave 6/29/14 5:17 PM
 */
@Slf4j
@RequiredArgsConstructor
public class OperationParser<T>{

    private final ConversionService conversionService;

    public Specification<T> parse(Map<String, Object> search) {
        Specifications spec = Specifications.where(new AndOrSpecification( SearchOperator.AND ));
        for(String key : search.keySet()){
            Specification<T> s = parse(key, search.get(key));
            spec = spec.and(s);
        }
        return spec;
    }

    public Specification<T> parse(String key, Object payload){
        String originalKey = key;
        SearchOperator op;
        String[] params = new String[0];

        if( key.startsWith("$") ){
            key = key.substring(1);

            // parse any params and set key to
            int pos = key.indexOf(":");
            if( pos >= 0){
                params = key.substring(pos + 1).split(":");
                key = key.substring(0, pos);
            }

            String token = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, key);
            op = SearchOperator.valueOf(token);

        }else{
            op = SearchOperator.MATCH;
            params = new String[]{key};
        }

        log.info("Looking for {} handler with params {} and payload {}", op, params, payload);
        return handle(op, params, payload);
    }

    private Specification<T> handle(SearchOperator op, String[] params, Object payload) {
        switch(op){
            case AND:
            case OR:

                Assert.isAssignable(Collection.class, payload.getClass(), "Must be a collection here");
                // probably should make sure our collection contains maps


                Specifications spec = Specifications.where(new AndOrSpecification(op));
                for(Map<String, Object> map : (Collection<Map<String, Object>>) payload){
                    Specification<?> s = parse(map);
                    if( op == SearchOperator.AND ){
                        spec = spec.and(s);
                    }else{
                        spec = spec.or(s);
                    }
                }

                return spec;

            case MATCH:
                return new MatchSpecification<T>(params[0], payload, conversionService);

            case LIKE:
                return new LikeSpecification<T>(params[0], payload, conversionService);

            case EL_MATCH:
                return new ElMatchSpecification<T>(params[0], params[1], payload, conversionService);


        }

        throw new IllegalStateException("Unhandled operation: " + op);
    }


}
