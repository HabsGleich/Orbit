package dev.habsgleich.orbit.repository;

import org.hibernate.Session;

@FunctionalInterface
public interface OrbitTransaction<T> {

    T perform(Session session, OrbitTransaction transaction);

}
