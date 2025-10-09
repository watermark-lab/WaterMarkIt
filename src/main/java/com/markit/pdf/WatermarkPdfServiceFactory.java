package com.markit.pdf;

import com.markit.servicelocator.Prioritizable;

import java.util.concurrent.Executor;

/**
 * Factory for creating {@link WatermarkPdfService} instances, optionally with an Executor.
 *
 * @author Oleg Cheban
 * @since 1.5.0
 */
public interface WatermarkPdfServiceFactory extends Prioritizable {

    /**
     * Create a {@link WatermarkPdfService} instance.
     *
     * @param executor optional executor to be used by the service; may be null
     * @return a {@link WatermarkPdfService}
     */
    WatermarkPdfService create(Executor executor);
}


