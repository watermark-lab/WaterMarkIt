package com.markit.api.handlers;

import com.markit.api.TextWatermarkAttributes;

import java.io.IOException;
import java.util.List;

/**
 * @author Oleg Cheban
 * @since 1.0
 */
@FunctionalInterface
public interface WatermarkHandler {
    byte[] apply(List<TextWatermarkAttributes> watermarks) throws IOException;
}
