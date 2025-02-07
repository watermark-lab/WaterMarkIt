package com.markit.servicelocator;

/**
 * @author Oleg Cheban
 * @since 1.3.2
 */
public interface Prioritizable {
    /**
     * the default priority
     */
    int DEFAULT_PRIORITY = 1;

    /**
     * Returns the priority of the implementation.
     * Higher values indicate higher priority.
     *
     * @return the priority of the implementation
     */
    int getPriority();
}
