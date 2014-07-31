package com.github.lemniscate.spring.search.spec;

import com.github.lemniscate.spring.search.SearchOperator;
import com.google.common.collect.Lists;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.Assert;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
* @Author dave 6/29/14 5:17 PM
*/
public class AndOrSpecification<T> implements Specification<T> {

    private final SearchOperator op;
    private final List<Specification> specifications;

    public AndOrSpecification(SearchOperator op, List<Specification> specifications) {
        Assert.notNull(op, "Op required");
        Assert.isTrue(op == SearchOperator.AND || op == SearchOperator.OR, "Unsupported op " + op);
        this.op = op;
        this.specifications = specifications;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = Lists.newArrayList();
        for(Specification s : specifications){
            predicates.add( s.toPredicate(root, query, cb) );
        }

        Predicate[] array = predicates.toArray(new Predicate[predicates.size()]);
        return op == SearchOperator.AND ? cb.and(array) : cb.or(array);
    }

    @Override
    public String toString() {
        return String.format("%s CONDITION", op);
    }

}
