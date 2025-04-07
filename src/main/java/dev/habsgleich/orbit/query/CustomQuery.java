package dev.habsgleich.orbit.query;

import jakarta.persistence.criteria.Root;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.hibernate.query.criteria.JpaPredicate;

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
     * @param root            Root of the query
     * @return Predicate to be used in the query
     */
    JpaPredicate query(HibernateCriteriaBuilder criteriaBuilder, Root<T> root);

}
