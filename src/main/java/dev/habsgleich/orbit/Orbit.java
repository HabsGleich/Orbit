package dev.habsgleich.orbit;

import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.jetbrains.annotations.ApiStatus;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
@Getter
@Accessors(fluent = true)
public class Orbit {

    private static final String JDBC_URL_PROPERTY_NAME = "hibernate.hikari.dataSource.url";
    private static final String JDBC_USER_PROPERTY_NAME = "hibernate.hikari.dataSource.user";
    private static final String JDBC_PASSWORD_PROPERTY_NAME = "hibernate.hikari.dataSource.password";

    @Getter
    private static Orbit instance;

    private final SessionFactory sessionFactory;

    @ApiStatus.Internal
    private Orbit(Properties props, Class<?>... entityClasses) {
        instance = this;

        final long start = System.currentTimeMillis();
        log.info("Initializing Orbit...");

        final Configuration config = new Configuration();
        config.setProperties(props);
        for (Class<?> entityClass : entityClasses) {
            config.addAnnotatedClass(entityClass);
        }

        this.sessionFactory = config.buildSessionFactory();

        System.out.println("Orbit successfully initialized in " + (System.currentTimeMillis() - start) + "ms");
    }

    public static void initialize(Properties props, Class<?>... entityClasses) {
        new Orbit(props, entityClasses);
    }

    public static void initialize(InputStream props, Class<?>... entityClasses) {
        try {
            Properties properties = new Properties();
            properties.load(props);

            initialize(properties, entityClasses);
        } catch (IOException e) {
            log.error("Could not load orbit.properties file, shutting down...", e);
            System.exit(1);
        }
    }

    public static void initialize(String url, String user, String password, Class<?>... entityClasses) {
        Properties props = new Properties();
        props.setProperty(JDBC_URL_PROPERTY_NAME, url);
        props.setProperty(JDBC_USER_PROPERTY_NAME, user);
        props.setProperty(JDBC_PASSWORD_PROPERTY_NAME, password);

        initialize(props, entityClasses);
    }
}
