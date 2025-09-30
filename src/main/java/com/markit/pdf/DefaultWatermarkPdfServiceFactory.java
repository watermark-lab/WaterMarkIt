package com.markit.pdf;

import java.util.concurrent.Executor;

/**
 * Default factory for creating {@link DefaultWatermarkPdfService} instances.
 */
public class DefaultWatermarkPdfServiceFactory implements WatermarkPdfServiceFactory {

    @Override
    public WatermarkPdfService create(Executor executor) {
        return new DefaultWatermarkPdfService(executor);
    }

    @Override
    public int getPriority() {
        return DEFAULT_PRIORITY;
    }
}


