package com.markit.api;

import com.markit.exceptions.ConvertBytesToBufferedImageException;
import com.markit.exceptions.EmptyWatermarkObjectException;
import com.markit.exceptions.WatermarkingException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class AbstractWatermarkService<S, B> {
    private static final Log logger = LogFactory.getLog(AbstractWatermarkService.class);
    protected WatermarkHandler watermarkHandler;
    protected final List<WatermarkAttributes> watermarks = new ArrayList<>();
    protected WatermarkAttributes currentWatermark;

    public B size(int size) {
        currentWatermark.setSize(size);
        return (B) this;
    }

    public B opacity(float opacity) {
        currentWatermark.setOpacity(opacity);
        return (B) this;
    }

    public B rotation(int degree) {
        currentWatermark.setRotation(degree);
        return (B) this;
    }

    public B when(boolean condition) {
        currentWatermark.setWatermarkEnabled(condition);
        return (B) this;
    }

    public B adjust(int x, int y) {
        var adjustment = new WatermarkPositionCoordinates.Coordinates(x, y);
        currentWatermark.setPositionAdjustment(adjustment);
        return (B) this;
    }

    @NotNull
    public byte[] apply() {
        try {
            and();
            return this.watermarkHandler.apply(this.watermarks);
        } catch (IOException e) {
            logger.error("Failed to watermark file", e);
            throw new WatermarkingException("Error watermarking the file", e);
        }
    }

    public S and() {
        if (currentWatermark.getText().isEmpty() && currentWatermark.getImage().isEmpty()) {
            logger.error("the watermark content is empty");
            throw new EmptyWatermarkObjectException();
        }
        watermarks.add(currentWatermark);
        currentWatermark = new WatermarkAttributes();
        return (S) this;
    }

    public Path apply(String directoryPath, String fileName) {
        if (!new File(directoryPath).isDirectory()) {
            logger.error(String.format("The directory does not exist or is not a directory: %s", directoryPath));
            throw new IllegalArgumentException("The directory does not exist or is not a directory.");
        }

        try {
            byte[] file = apply();
            Path filePath = Paths.get(directoryPath, fileName);

            return Files.write(filePath, file);
        } catch (IOException e) {
            logger.error("Failed to watermark file", e);
            throw new WatermarkingException("Error watermarking the file", e);
        } catch (ConvertBytesToBufferedImageException e) {
            logger.error("Failed to convert bytes to buffered image", e);
            throw new WatermarkingException("Error converting bytes to buffered image", e);
        }
    }
}
