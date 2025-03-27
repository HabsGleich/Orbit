package dev.habsgleich.orbit.helper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.lang.annotation.Annotation;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ReflectionHelper {

    private static final Reflections REFLECTIONS = new Reflections(new ConfigurationBuilder()
        .addUrls(ClasspathHelper.forJavaClassPath())
        .addScanners(Scanners.SubTypes, Scanners.TypesAnnotated)
    );

    /**
     * Scans the classpath for all classes annotated with the given annotation.
     *
     * @param annotation The annotation to scan for.
     * @return A set of all classes annotated with the given annotation.
     */
    public static synchronized Set<Class<?>> scanForAnnotatedClasses(Class<? extends Annotation> annotation) {
        return REFLECTIONS.getTypesAnnotatedWith(annotation);
    }

}
