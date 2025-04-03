package dev.habsgleich.orbit;

import dev.habsgleich.orbit.helper.ReflectionHelper;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.jetbrains.annotations.ApiStatus;

import javax.persistence.Entity;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.Properties;
import java.util.Set;

/**
 * Orbit is a simple and lightweight ORM for Java.
 */
@Slf4j
@Getter
@Accessors(fluent = true)
public class Orbit {

    private static final String JDBC_URL_PROPERTY_NAME = "hibernate.hikari.dataSource.url";
    private static final String JDBC_USER_PROPERTY_NAME = "hibernate.hikari.dataSource.user";
    private static final String JDBC_PASSWORD_PROPERTY_NAME = "hibernate.hikari.dataSource.password";

    @Getter
    private static Orbit instance;

    private final Properties properties;
    private Configuration configuration;

    private SessionFactory sessionFactory;

    @SafeVarargs
    @ApiStatus.Internal
    private Orbit(Properties props, Collection<URL>... urls) {
        instance = this;

        this.properties = props;

        final long start = System.currentTimeMillis();
        log.info("Initializing Orbit...");

        final Configuration config = new Configuration();
        config.setProperties(props);

        this.configuration = config;

        final Set<Class<?>> entityClasses = new ReflectionHelper(urls).scanForAnnotatedClasses(Entity.class);
        for (Class<?> entityClass : entityClasses) {
            log.info("Found annotated Entity '{}', registering in Orbit...", entityClass.getName());
            config.addAnnotatedClass(entityClass);
        }

        this.sessionFactory = config.buildSessionFactory();

        log.info("Orbit successfully initialized in {}ms", System.currentTimeMillis() - start);
    }

    /**
     * Initialize Orbit with the given properties and entity classes.
     *
     * @param props Properties to initialize Orbit with
     */
    @SafeVarargs
    public static void initialize(Properties props, Collection<URL>... urls) {
        new Orbit(props, urls);
    }

    /**
     * Initialize Orbit with the given properties file from an {@link InputStream} and entity classes.
     *
     * @param props InputStream of the properties file to initialize Orbit with
     */
    @SafeVarargs
    public static void initialize(InputStream props, Collection<URL>... urls) {
        try {
            Properties properties = new Properties();
            properties.load(props);

            initialize(properties, urls);
        } catch (IOException e) {
            log.error("Could not load orbit.properties file, shutting down...", e);
            System.exit(1);
        }
    }

    /**
     * Initialize Orbit with the given credentials and entity classes.
     *
     * @param url JDBC URL to connect to the database
     * @param user Username to connect to the database
     * @param password Password to connect to the database
     */
    @SafeVarargs
    public static void initialize(String url, String user, String password, Collection<URL>... urls) {
        Properties props = new Properties();
        props.setProperty(JDBC_URL_PROPERTY_NAME, url);
        props.setProperty(JDBC_USER_PROPERTY_NAME, user);
        props.setProperty(JDBC_PASSWORD_PROPERTY_NAME, password);

        initialize(props, urls);
    }

    /**
     * Register additional entity classes with Orbit.
     *
     * @param entities Entity classes to register
     */
    public void registerEntities(Class<?>... entities) {
        this.configuration = new Configuration();
        this.configuration.setProperties(this.properties);

        for (Class<?> entity : entities) {
            if (entity.isAnnotationPresent(Entity.class)) {
                configuration.addAnnotatedClass(entity);
                log.info("Registered entity class: {}", entity.getName());
            } else {
                throw new IllegalArgumentException("Entity '" + entity.getName() + "' is not annotated with @" + Entity.class.getName());
            }
        }
        if (this.sessionFactory != null && this.sessionFactory.isOpen()) {
            this.sessionFactory.close();
        }
        this.sessionFactory = this.configuration.buildSessionFactory();
    }
}
