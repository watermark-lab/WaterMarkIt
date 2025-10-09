package com.markit.api.builders;

import com.markit.api.WatermarkAttributes;
import com.markit.api.WatermarkProcessor;
import com.markit.exceptions.WatermarkingException;
import com.markit.utils.ValidationUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Base service for applying watermarks to files.
 *
 * @param <T> The concrete service type that extends this class
 * @author Oleg Cheban
 * @since 1.3.3
 */
public class BaseWatermarkBuilder<T> {

    private final WatermarkProcessor watermarkProcessor;

    private final List<WatermarkAttributes> watermarks = new ArrayList<>();

    private WatermarkAttributes watermark;

    protected BaseWatermarkBuilder(WatermarkProcessor watermarkProcessor) {
        this.watermark = new WatermarkAttributes();
        this.watermarkProcessor = Objects.requireNonNull(watermarkProcessor, "WatermarkProcessor must not be null");
    }

    /**
     * Adds the watermark to the list and prepares for configuring another one.
     *
     * @return The service instance
     * @throws IllegalArgumentException if the current watermark attributes are invalid
     */
    public T and() {
        approvePreviousWatermarkAttributes();
        watermark = new WatermarkAttributes();
        @SuppressWarnings("unchecked")
        var service = (T) this;
        return service;
    }

    /**
     * Applies all configured watermarks to the file.
     *
     * @return The watermarked file as a byte array
     * @throws WatermarkingException if an error occurs during watermarking
     */
    public byte[] apply() {
        try {
            approvePreviousWatermarkAttributes();
            return this.watermarkProcessor.apply(this.watermarks);
        } catch (IOException e) {
            throw new WatermarkingException("Error watermarking the file", e);
        }
    }

    protected WatermarkAttributes getWatermark() {
        return watermark;
    }

    private void approvePreviousWatermarkAttributes(){
        Objects.requireNonNull(watermark, "Current watermark must not be null");
        boolean isValid = ValidationUtils.validateWatermarkAttributes(watermark);
        if (!isValid) {
            throw new IllegalArgumentException("Invalid watermark attributes");
        }
        watermarks.add(watermark);
    }
}
