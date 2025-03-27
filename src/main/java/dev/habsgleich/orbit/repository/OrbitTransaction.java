package dev.habsgleich.orbit.repository;

import org.hibernate.Session;

/**
 * A transaction to be performed on the database.
 *
 * @param <T> Type of the transaction result
 */
@FunctionalInterface
public interface OrbitTransaction<T> {

    /**
     * Perform the transaction on the database.
     *
     * @param session Session to be used for the transaction
     * @return Result of the transaction
     */
    T perform(Session session, OrbitTransaction<T> transaction);

}
