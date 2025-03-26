package dev.habsgleich.orbit.query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QueryBuilder<T> {

    private final List<String> fetchPaths = new ArrayList<>();
    private final List<Predicate> predicates = new ArrayList<>();
    private final Session session;
    private final CriteriaBuilder builder;
    private final CriteriaQuery<T> criteria;
    private final Root<T> root;

    public QueryBuilder(SessionFactory sessionFactory, Class<T> entityClass) {
        this.session = sessionFactory.openSession();
        this.builder = session.getCriteriaBuilder();
        this.criteria = builder.createQuery(entityClass);
        this.root = criteria.from(entityClass);
    }

    public QueryBuilder<T> fetch(String path) {
        fetchPaths.add(path);
        return this;
    }

    public QueryBuilder<T> equal(String property, Object value) {
        predicates.add(builder.equal(root.get(property), value));
        return this;
    }

    public QueryBuilder<T> like(String property, String value) {
        predicates.add(builder.like(root.get(property), value));
        return this;
    }

    public QueryBuilder<T> greaterThan(String property, Number value) {
        predicates.add(builder.gt(root.get(property), value));
        return this;
    }

    public QueryBuilder<T> lessThan(String property, Number value) {
        predicates.add(builder.lt(root.get(property), value));
        return this;
    }

    public QueryBuilder<T> custom(CustomQuery<T> customQuery) {
        predicates.add(customQuery.query(this.builder, this.root));
        return this;
    }

    public Optional<T> findOne() {
        try {
            applyFetchPaths();
            applyPredicates();
            return Optional.ofNullable(session.createQuery(criteria).uniqueResult());
        } finally {
            session.close();
        }
    }

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
            criteria.where(builder.and(predicates.toArray(new Predicate[0])));
        }
    }

}
