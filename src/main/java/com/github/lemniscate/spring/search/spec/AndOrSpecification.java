package com.github.lemniscate.spring.search.spec;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.Assert;
import com.github.lemniscate.spring.search.SearchOperator;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
* @Author dave 6/29/14 5:17 PM
*/
public class AndOrSpecification<T> implements Specification<T> {

    private final SearchOperator op;

    public AndOrSpecification(SearchOperator op) {
        Assert.notNull(op, "Op required");
        Assert.isTrue(op == SearchOperator.AND || op == SearchOperator.OR, "Unsupported op " + op);
        this.op = op;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        return op == SearchOperator.AND ? cb.and() : cb.or();
    }

    @Override
    public String toString() {
        return String.format("%s CONDITION", op);
    }
}
