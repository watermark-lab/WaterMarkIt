package com.markit.core;

import com.markit.exceptions.WatermarkingException;
import com.markit.utils.ValidationUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Oleg Cheban
 * @since 1.3.3
 */
public class BaseWatermarkService<T> {

    protected WatermarkHandler watermarkHandler;

    protected final List<WatermarkAttributes> watermarks = new ArrayList<>();

    protected WatermarkAttributes currentWatermark;

    public T and() {
        ValidationUtils.validateWatermarkAttributes(currentWatermark);
        watermarks.add(currentWatermark);
        currentWatermark = new WatermarkAttributes();
        @SuppressWarnings("unchecked")
        var service = (T) this;
        return service;
    }

    @NotNull
    public byte[] apply() {
        try {
            and();
            return this.watermarkHandler.apply(this.watermarks);
        } catch (IOException e) {
            throw new WatermarkingException("Error watermarking the file", e);
        }
    }
}
