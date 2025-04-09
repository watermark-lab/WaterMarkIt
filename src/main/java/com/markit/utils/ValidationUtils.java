package com.markit.utils;

import com.markit.api.WatermarkAttributes;

import java.io.File;

/**
 * @author Oleg Cheban
 * @since 1.3.3
 */
public class ValidationUtils {

    public static void validateWatermarkAttributes(WatermarkAttributes watermark) {
        if (watermark.getText().isEmpty() && watermark.getImage().isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    public static void validateDirectory(String directoryPath) {
        File file = new File(directoryPath);
        if (!file.isDirectory()) {
            throw new IllegalArgumentException("The directory does not exist or is not a directory.");
        }
    }
}
