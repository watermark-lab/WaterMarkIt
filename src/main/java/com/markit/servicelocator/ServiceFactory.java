package com.markit.servicelocator;

import java.util.*;

/**
 * @author Oleg Cheban
 * @since 1.3.2
 */
public class ServiceFactory<T extends Prioritizable> {
    private static final ServiceFactory<Prioritizable> instance = new ServiceFactory<>();
    private final Map<Class<? extends T>, T> serviceInstances = new HashMap<>();
    public static ServiceFactory<Prioritizable> getInstance() {
        return instance;
    }

    private ServiceFactory() {
    }

    public T getService(Class<? extends T> clazz) {
        if (serviceInstances.containsKey(clazz)){
            return serviceInstances.get(clazz);
        }

        final Set<T> serviceClasses = new TreeSet<>((o1, o2) -> -1 * Integer.compare(o1.getPriority(), o2.getPriority()));
        serviceClasses.addAll(DefaultServiceLocator.find(clazz));

        try {
            @SuppressWarnings("unchecked")
            T service = (T) serviceClasses.iterator().next().getClass().getConstructor().newInstance();
            this.serviceInstances.put(clazz, service);
            return service;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
