package com.markit.servicelocator;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Oleg Cheban
 * @since 1.3.2
 */
public class ServiceFactory<T extends Prioritizable> {
    private static final ServiceFactory<Prioritizable> instance = new ServiceFactory<>();
    private final Map<Class<? extends T>, T> serviceInstances = new ConcurrentHashMap<>();
    public static ServiceFactory<Prioritizable> getInstance() {
        return instance;
    }

    private ServiceFactory() {
    }

    public T getService(Class<? extends T> clazz) {
        return serviceInstances.computeIfAbsent(clazz, this::loadHighestPriorityService);
    }

    private T loadHighestPriorityService(Class<? extends T> clazz) {
        return DefaultServiceLocator.find(clazz).stream()
                .max(Comparator.comparingInt(Prioritizable::getPriority))
                .orElseThrow(() -> new NoSuchElementException(
                        "No service implementation found for: " + clazz.getName()));
    }
}
