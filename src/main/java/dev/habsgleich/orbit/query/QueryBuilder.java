package dev.habsgleich.orbit.query;

import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.hibernate.query.criteria.JpaCriteriaQuery;
import org.hibernate.query.criteria.JpaPredicate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A query builder to make queries to the database.
 *
 * @param <T> Type of the entity to be queried
 */
public class QueryBuilder<T> {

    private final List<String> fetchPaths = new ArrayList<>();
    private final List<JpaPredicate> predicates = new ArrayList<>();
    private final Session session;
    private final HibernateCriteriaBuilder builder;
    private final JpaCriteriaQuery<T> criteria;
    private final Root<T> root;

    public QueryBuilder(SessionFactory sessionFactory, Class<T> entityClass) {
        this.session = sessionFactory.openSession();
        this.builder = session.getCriteriaBuilder();
        this.criteria = builder.createQuery(entityClass);
        this.root = criteria.from(entityClass);
    }

    /**
     * A relation to be fetched with the query.
     *
     * @param path
     * @return
     */
    public QueryBuilder<T> fetch(String path) {
        fetchPaths.add(path);
        return this;
    }

    /**
     * Create an equal (=) predicate for the query.
     *
     * @param property field in the entity
     * @param value    value to be compared
     * @return the fluent builder
     */
    public QueryBuilder<T> equal(String property, Object value) {
        predicates.add(builder.equal(root.get(property), value));
        return this;
    }

    /**
     * Create a like predicate for the query.
     *
     * @param property field in the entity
     * @param value    value to be compared
     * @return the fluent builder
     */
    public QueryBuilder<T> like(String property, String value) {
        predicates.add(builder.like(root.get(property), value));
        return this;
    }

    /**
     * Create a greater than (>) predicate for the query.
     *
     * @param property field in the entity
     * @param value    value to be compared
     * @return the fluent builder
     */
    public QueryBuilder<T> greaterThan(String property, Number value) {
        predicates.add(builder.gt(root.get(property), value));
        return this;
    }

    /**
     * Create a less than (<) predicate for the query.
     *
     * @param property field in the entity
     * @param value    value to be compared
     * @return the fluent builder
     */
    public QueryBuilder<T> lessThan(String property, Number value) {
        predicates.add(builder.lt(root.get(property), value));
        return this;
    }

    /**
     * Make a custom query using a {@link CustomQuery}.
     *
     * @param customQuery custom query to be made
     * @return the fluent builder
     */
    public QueryBuilder<T> custom(CustomQuery<T> customQuery) {
        predicates.add(customQuery.query(this.builder, this.root));
        return this;
    }

    /**
     * Find one entity using the query.
     *
     * @return the entity if found
     */
    public Optional<T> findOne() {
        try {
            applyFetchPaths();
            applyPredicates();
            return Optional.ofNullable(session.createQuery(criteria).uniqueResult());
        } finally {
            session.close();
        }
    }

    /**
     * Find all entities using the query.
     *
     * @return the list of entities
     */
    public List<T> findAll() {
        try {
            applyFetchPaths();
            applyPredicates();
            return session.createQuery(criteria).getResultList();
        } finally {
            session.close();
        }
    }

    private void applyFetchPaths() {
        for (String path : fetchPaths) {
            root.fetch(path, JoinType.LEFT);
        }
    }

    private void applyPredicates() {
        if (!predicates.isEmpty()) {
            criteria.where(builder.and(predicates.toArray(new JpaPredicate[0])));
        }
    }

}
