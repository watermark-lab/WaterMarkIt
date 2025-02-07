package com.markit.servicelocator;

import java.util.*;

/**
 * Utility class for locating and retrieving instances of a given interface type using the {@link ServiceLoader} mechanism.
 * This class provides a static method to find all implementations of a specified interface available in the classpath.
 *
 * @author Oleg Cheban
 * @since 1.3.2
 */
public class DefaultServiceLocator {
    public static <T> List<T> findInstances(Class<T> interfaceType) {
        List<T> allInstances = new ArrayList<>();
        final Iterator<T> services = ServiceLoader.load(interfaceType, Thread.currentThread().getContextClassLoader()).iterator();

        while (services.hasNext()) {
            try {
                final T service = services.next();
                allInstances.add(service);
            } catch (Throwable e) {
                throw new RuntimeException();
            }
        }

        return Collections.unmodifiableList(allInstances);
    }
}
