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

    public static <T> Repository<T> of(Class<T> entityClass) {
        /*SessionFactoryImpl sessionFactoryImpl = (SessionFactoryImpl) Orbit.instance().sessionFactory();
        MetadataSources metadataSources = new MetadataSources();
        metadataSources.addAnnotatedClass(entityClass);

        Metadata metadata = metadataSources.getMetadataBuilder().build();
        sessionFactoryImpl.getMetamodel().*/
        return new Repository<>(entityClass);
    }

    public QueryBuilder<T> query() {
        return new QueryBuilder<>(Orbit.instance().sessionFactory(), this.entityClass);
    }

    public T merge(T entity) {
        return this.transactional((session, transaction) -> (T) session.merge(entity));
    }

    public void delete(T entity) {
        this.transactional((session, transaction) -> {
            session.delete(entity);
            return null;
        });
    }

    public T transactional(OrbitTransaction<T> transaction) {
        Session session = null;
        Transaction hibernateTransaction = null;

        T result = null;
        try {
            session = Orbit.instance().sessionFactory().openSession();
            hibernateTransaction = session.beginTransaction();

            result = transaction.perform(session, transaction);

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
