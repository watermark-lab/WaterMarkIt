package com.markit.video.ffmpeg.filters;

import com.markit.servicelocator.DefaultServiceLocator;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Factory class for retrieving FilterStepBuilder implementations based on Step type.
 *
 * @author Oleg Cheban
 * @since 1.4.0
 */
public class FilterStepBuilderFactory {

    private static final FilterStepBuilderFactory instance = new FilterStepBuilderFactory();

    private final Map<FilterStepType, FilterStepBuilder> cachedBuilders = new HashMap<>();

    private FilterStepBuilderFactory() {
    }

    public static FilterStepBuilderFactory getInstance() {
        return instance;
    }

    /**
     * Retrieves the highest priority FilterStepBuilder for the given Step type.
     *
     * @param filterStepType the Step type (OVERLAY or TEXT)
     * @return the highest priority FilterStepBuilder for the given type
     * @throws RuntimeException if no builder is found for the given step type
     */
    public FilterStepBuilder getBuilder(FilterStepType filterStepType) {
        return cachedBuilders.computeIfAbsent(filterStepType, this::findHighestPriorityBuilder);
    }

    private FilterStepBuilder findHighestPriorityBuilder(FilterStepType filterStepType) {
        return DefaultServiceLocator.find(FilterStepBuilder.class).stream()
                .filter(builder -> builder.getFilterStepType() == filterStepType)
                .max(Comparator.comparingInt(FilterStepBuilder::getPriority))
                .orElseThrow(() -> new RuntimeException(
                        "No FilterStepBuilder found for step type: " + filterStepType));
    }
}
