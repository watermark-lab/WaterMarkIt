package com.markit.video.ffmpeg.filters;

import com.markit.servicelocator.DefaultServiceLocator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        if (cachedBuilders.containsKey(filterStepType)) {
            return cachedBuilders.get(filterStepType);
        }

        // Load all FilterStepBuilder implementations
        List<FilterStepBuilder> allBuilders = DefaultServiceLocator.find(FilterStepBuilder.class);

        // Filter by step type and sort by priority (highest first)
        List<FilterStepBuilder> buildersForStep = allBuilders.stream()
                .filter(builder -> builder.getFilterStepType() == filterStepType)
                .sorted((o1, o2) -> -1 * Integer.compare(o1.getPriority(), o2.getPriority()))
                .collect(Collectors.toList());

        if (buildersForStep.isEmpty()) {
            throw new RuntimeException("No FilterStepBuilder found for step type: " + filterStepType);
        }

        // Get the highest priority builder
        FilterStepBuilder builder = buildersForStep.get(0);
        cachedBuilders.put(filterStepType, builder);

        return builder;
    }
}
