package com.markit.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Optional;

public class ValidationUtils {

    public static void validateCurrentWatermark(WatermarkAttributes watermark) {
        if (watermark.getText().isEmpty() && watermark.getImage().isEmpty()) {
            throw new EmptyWatermarkObjectException();
        }
    }

    public static void validateDirectory(String directoryPath) {
        File file = new File(directoryPath);
        if (!file.isDirectory()) {
            throw new IllegalArgumentException("The directory does not exist or is not a directory.");
        }
    }

    public static BufferedImage validateImage(Object imageSource) throws ConvertBytesToBufferedImageException {
        if (imageSource instanceof byte[]) {
            return ImageConverter.convertToBufferedImage((byte[]) imageSource);
        } else if (imageSource instanceof File) {
            return ImageConverter.convertToBufferedImage((File) imageSource);
        } else if (imageSource instanceof BufferedImage) {
            return (BufferedImage) imageSource;
        }
        throw new IllegalArgumentException("Unsupported file source type: " + imageSource.getClass().getName());
    }

    public static void validateWatermarkAttributes(WatermarkAttributes watermark, String errorMessage) {
        if (!watermark.getText().isEmpty() && !watermark.getImage().isEmpty()) {
            throw new IllegalArgumentException(errorMessage);
        }
    }
}
