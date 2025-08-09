package com.markit.api;

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
public class BaseWatermarkService<T> {

    private final WatermarkHandler watermarkHandler;

    private final List<WatermarkAttributes> watermarks = new ArrayList<>();

    private WatermarkAttributes watermark;

    protected BaseWatermarkService(WatermarkHandler watermarkHandler) {
        this.watermark = new WatermarkAttributes();
        this.watermarkHandler = Objects.requireNonNull(watermarkHandler, "WatermarkHandler must not be null");
    }

    /**
     * Adds the watermark to the list and prepares for configuring another one.
     *
     * @return The service instance
     * @throws IllegalArgumentException if the current watermark attributes are invalid
     */
    public T and() {
        approveWatermark();
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
            approveWatermark();
            return this.watermarkHandler.apply(this.watermarks);
        } catch (IOException e) {
            throw new WatermarkingException("Error watermarking the file", e);
        }
    }

    protected WatermarkAttributes getWatermark() {
        return watermark;
    }

    private void approveWatermark(){
        Objects.requireNonNull(watermark, "Current watermark must not be null");
        boolean isValid = ValidationUtils.validateWatermarkAttributes(watermark);
        if (!isValid) {
            throw new IllegalArgumentException("Invalid watermark attributes");
        }
        watermarks.add(watermark);
    }
}
