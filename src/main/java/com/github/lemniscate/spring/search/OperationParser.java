package com.github.lemniscate.spring.search;

import com.github.lemniscate.spring.search.spec.AndOrSpecification;
import com.google.common.base.CaseFormat;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Map;

/**
 * @Author dave 6/29/14 5:17 PM
 */
@Slf4j
@RequiredArgsConstructor
public class OperationParser<T>{

    public static final String COMMAND_TOKEN = "~";

    private final ConversionService conversionService;

    public Specification<T> parse(Map<String, Object> search) {
        return parse(search, false);
    }

    public Specification<T> parse(Map<String, Object> search, boolean distinct) {
        List<Specification> specs = Lists.newArrayList();
        for(String key : search.keySet()){
            Specification<T> s = parse(key, search.get(key));
            specs.add(s);
        }

        AndOrSpecification spec = new AndOrSpecification(SearchOperator.AND, specs, distinct);
        return spec;
    }

    public Specification<T> parse(String key, Object payload){
        SearchOperator op;
        String[] params = new String[0];

        if( key.startsWith( COMMAND_TOKEN ) ){
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
        return op.getSpecification(params, payload, this, conversionService);
    }
}
