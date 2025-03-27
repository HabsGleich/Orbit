package dev.habsgleich.orbit.query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * A method to make custom predicates for queries.
 * Complex queries can be made using this method.
 *
 * @param <T> Type of the entity to be queried
 */
@FunctionalInterface
public interface CustomQuery<T> {

    /**
     * Query method to make custom predicates.
     *
     * @param criteriaBuilder CriteriaBuilder to build the query
     * @param root Root of the query
     * @return Predicate to be used in the query
     */
    Predicate query(CriteriaBuilder criteriaBuilder, Root<T> root);

}
