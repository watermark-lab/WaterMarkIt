package com.markit.api;

import com.markit.exceptions.ConvertBytesToBufferedImageException;
import com.markit.exceptions.EmptyWatermarkObjectException;
import com.markit.exceptions.WatermarkingException;
import com.markit.image.ImageConverter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Oleg Cheban
 * @since 1.3.0
 */
@SuppressWarnings("unchecked")
public abstract class AbstractWatermarkService<Service, Builder, TextBasedWatermarkBuilder, PositionStepBuilder> {
    private static final Log logger = LogFactory.getLog(AbstractWatermarkService.class);
    protected WatermarkHandler watermarkHandler;
    protected final List<WatermarkAttributes> watermarks = new ArrayList<>();
    protected WatermarkAttributes currentWatermark;

    public AbstractWatermarkService() {
        this.currentWatermark = new WatermarkAttributes();
    }

    public TextBasedWatermarkBuilder withText(String text) {
        currentWatermark.setText(text);
        return (TextBasedWatermarkBuilder) this;
    }

    public Builder withImage(byte[] image) {
        var imageConverter = new ImageConverter();
        currentWatermark.setImage(Optional.of(imageConverter.convertToBufferedImage(image)));
        return (Builder) this;
    }

    public Builder withImage(BufferedImage image) {
        currentWatermark.setImage(Optional.of(image));
        return (Builder) this;
    }

    public Builder withImage(File image) {
        var imageConverter = new ImageConverter();
        currentWatermark.setImage(Optional.of(imageConverter.convertToBufferedImage(image)));
        return (Builder) this;
    }

    public TextBasedWatermarkBuilder color(Color color) {
        currentWatermark.setColor(color);
        return (TextBasedWatermarkBuilder) this;
    }

    public TextBasedWatermarkBuilder addTrademark() {
        currentWatermark.setTrademark(true);
        return (TextBasedWatermarkBuilder) this;
    }

    public Builder size(int size) {
        currentWatermark.setSize(size);
        return (Builder) this;
    }

    public Builder opacity(float opacity) {
        currentWatermark.setOpacity(opacity);
        return (Builder) this;
    }

    public Builder rotation(int degree) {
        currentWatermark.setRotation(degree);
        return (Builder) this;
    }

    public Builder when(boolean condition) {
        currentWatermark.setWatermarkEnabled(condition);
        return (Builder) this;
    }

    public Builder adjust(int x, int y) {
        var adjustment = new WatermarkPositionCoordinates.Coordinates(x, y);
        currentWatermark.setPositionAdjustment(adjustment);
        return (Builder) this;
    }

    public Builder watermark() {
        return (Builder) this;
    }

    public PositionStepBuilder position(WatermarkPosition watermarkPosition) {
        currentWatermark.setPosition(watermarkPosition);
        return (PositionStepBuilder) this;
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

    public Service and() {
        validateCurrentWatermark();
        watermarks.add(currentWatermark);
        currentWatermark = new WatermarkAttributes();
        return (Service) this;
    }

    public Path apply(String directoryPath, String fileName) {
        validateDirectory(directoryPath);
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

    private void validateCurrentWatermark() {
        if (currentWatermark.getText().isEmpty() && currentWatermark.getImage().isEmpty()) {
            logger.error("The watermark content is empty");
            throw new EmptyWatermarkObjectException();
        }
    }

    private void validateDirectory(String directoryPath) {
        if (!new File(directoryPath).isDirectory()) {
            logger.error(String.format("Invalid directory: %s", directoryPath));
            throw new IllegalArgumentException("The directory does not exist or is not a directory.");
        }
    }
}
