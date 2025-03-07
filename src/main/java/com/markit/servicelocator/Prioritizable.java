package com.markit.servicelocator;

/**
 *  The interface using for the services that are created via factories, utilizing the Java ServiceLoader mechanism
 *  It's necessary when multiple implementations of a service are loaded, and selection based on priority is required.
 *
 * @author Oleg Cheban
 * @since 1.3.2
 */
public interface Prioritizable {

    int DEFAULT_PRIORITY = 1;

    /**
     * Returns the priority of the implementation.
     * Higher values indicate higher priority.
     *
     * @return the priority of the implementation
     */
    int getPriority();
}
