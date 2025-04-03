package dev.habsgleich.orbit.helper;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Stream;

public final class ReflectionHelper {

    private final Reflections reflections;

    @SafeVarargs
    public ReflectionHelper(Collection<URL>... urls) {
        this.reflections = new Reflections(new ConfigurationBuilder()
            .addUrls(Stream.of(urls).flatMap(Collection::stream).toArray(URL[]::new))
            .addScanners(Scanners.SubTypes, Scanners.TypesAnnotated)
        );
    }

    /**
     * Scans the classpath for all classes annotated with the given annotation.
     *
     * @param annotation The annotation to scan for.
     * @return A set of all classes annotated with the given annotation.
     */
    public synchronized Set<Class<?>> scanForAnnotatedClasses(Class<? extends Annotation> annotation) {
        return reflections.getTypesAnnotatedWith(annotation);
    }

}
