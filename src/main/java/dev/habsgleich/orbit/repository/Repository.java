package dev.habsgleich.orbit.repository;

import dev.habsgleich.orbit.Orbit;
import dev.habsgleich.orbit.query.QueryBuilder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.Transaction;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Repository<T> {

    private final Class<T> entityClass;

    /**
     * Create a repository for the given entity class.
     *
     * @param entityClass Entity class to create the repository for
     * @param <T> Type of the entity
     * @return Repository for the entity class
     */
    public static <T> Repository<T> of(Class<T> entityClass) {
        /*SessionFactoryImpl sessionFactoryImpl = (SessionFactoryImpl) Orbit.instance().sessionFactory();
        MetadataSources metadataSources = new MetadataSources();
        metadataSources.addAnnotatedClass(entityClass);

        Metadata metadata = metadataSources.getMetadataBuilder().build();
        sessionFactoryImpl.getMetamodel().*/
        return new Repository<>(entityClass);
    }

    /**
     * Start a fluent query builder for the entity class.
     *
     * @return Query builder for the entity class
     */
    public QueryBuilder<T> query() {
        return new QueryBuilder<>(Orbit.instance().sessionFactory(), this.entityClass);
    }

    /**
     * Upsert the entity to the database.
     * If the entity exists, it will be updated.
     * Otherwise, it will be inserted.
     *
     * @param entity Entity to be upserted
     * @return Upserted entity
     */
    public T merge(T entity) {
        return this.transactional((session, transaction) -> (T) session.merge(entity));
    }

    /**
     * Delete the entity from the database.
     *
     * @param entity Entity to be deleted
     */
    public void delete(T entity) {
        this.transactional((session, transaction) -> {
            session.remove(entity);
            return null;
        });
    }

    /**
     * Perform a transaction on the database.
     *
     * @param transaction Transaction to be performed
     * @return Result of the transaction
     */
    public T transactional(OrbitTransaction<T> transaction) {
        Session session = null;
        Transaction hibernateTransaction = null;

        try {
            session = Orbit.instance().sessionFactory().openSession();
            hibernateTransaction = session.beginTransaction();

            T result = transaction.perform(session, transaction);

            hibernateTransaction.commit();
            return result;
        } catch (Exception e) {
            if (hibernateTransaction != null) {
                hibernateTransaction.rollback();
            }
            throw new RuntimeException(e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

}
