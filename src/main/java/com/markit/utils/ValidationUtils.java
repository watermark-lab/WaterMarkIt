package com.markit.utils;

import com.markit.api.WatermarkAttributes;

/**
 * @author Oleg Cheban
 * @since 1.3.3
 */
public class ValidationUtils {

    public static boolean validateWatermarkAttributes(WatermarkAttributes watermark) {
        return !(watermark.getText().isEmpty() && watermark.getImage().isEmpty());
    }
}
