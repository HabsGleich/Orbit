package dev.habsgleich.orbit.query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@FunctionalInterface
public interface CustomQuery<T> {

    Predicate query(CriteriaBuilder criteriaBuilder, Root<T> root);

}
