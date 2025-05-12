package com.markit.utils;

import com.markit.core.WatermarkAttributes;

/**
 * @author Oleg Cheban
 * @since 1.3.3
 */
public class ValidationUtils {

    public static void validateWatermarkAttributes(WatermarkAttributes watermark) {
        if (watermark.getText().isEmpty() && watermark.getImage().isEmpty() && watermark.getAudio().isEmpty()) {
            throw new IllegalArgumentException();
        }
    }
}
