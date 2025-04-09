package com.markit.api;

import com.markit.api.builders.PositionStepBuilder;
import com.markit.api.builders.TextBasedWatermarkBuilder;
import com.markit.api.positioning.WatermarkPosition;
import com.markit.api.positioning.WatermarkPositionCoordinates;
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
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * @author Oleg Cheban
 * @since 1.3.0
 */
@SuppressWarnings("unchecked")
public abstract class AbstractWatermarkService<Service, WatermarkBuilder>
        implements PositionStepBuilder<WatermarkBuilder>, TextBasedWatermarkBuilder<WatermarkBuilder> {
    private static final Log logger = LogFactory.getLog(AbstractWatermarkService.class);
    protected WatermarkHandler watermarkHandler;
    protected final List<WatermarkAttributes> watermarks = new ArrayList<>();
    protected WatermarkAttributes currentWatermark;

    public AbstractWatermarkService() {
        this.currentWatermark = new WatermarkAttributes();
    }

    public TextBasedWatermarkBuilder<WatermarkBuilder> withText(String text) {
        Objects.requireNonNull(text);
        currentWatermark.setText(text);
        return this;
    }

    public WatermarkBuilder withImage(byte[] image) {
        Objects.requireNonNull(image);
        var imageConverter = new ImageConverter();
        return withImage(() -> imageConverter.convertToBufferedImage(image));
    }

    public WatermarkBuilder withImage(BufferedImage image) {
        Objects.requireNonNull(image);
        return withImage(() -> image);
    }

    public WatermarkBuilder withImage(File image) {
        Objects.requireNonNull(image);
        var imageConverter = new ImageConverter();
        return withImage(() -> imageConverter.convertToBufferedImage(image));
    }

    private WatermarkBuilder withImage(Supplier<BufferedImage> imageSupplier) {
        currentWatermark.setImage(Optional.of(imageSupplier.get()));
        return (WatermarkBuilder) this;
    }

    public TextBasedWatermarkBuilder<WatermarkBuilder> color(Color color) {
        Objects.requireNonNull(color);
        currentWatermark.setColor(color);
        return this;
    }

    public TextBasedWatermarkBuilder<WatermarkBuilder> addTrademark() {
        currentWatermark.setTrademark(true);
        return this;
    }

    public WatermarkBuilder size(int size) {
        currentWatermark.setSize(size);
        return (WatermarkBuilder) this;
    }

    public WatermarkBuilder opacity(int opacity) {
        currentWatermark.setOpacity(opacity);
        return (WatermarkBuilder) this;
    }

    public WatermarkBuilder rotation(int degree) {
        currentWatermark.setRotationDegrees(degree);
        return (WatermarkBuilder) this;
    }

    public WatermarkBuilder when(boolean condition) {
        currentWatermark.setWatermarkEnabled(condition);
        return (WatermarkBuilder) this;
    }

    public PositionStepBuilder<WatermarkBuilder> position(WatermarkPosition watermarkPosition) {
        Objects.requireNonNull(watermarkPosition);
        currentWatermark.setPosition(watermarkPosition);
        return this;
    }

    public PositionStepBuilder<WatermarkBuilder> adjust(int x, int y) {
        var adjustment = new WatermarkPositionCoordinates.Coordinates(x, y);
        currentWatermark.setPositionAdjustment(adjustment);
        return this;
    }

    public PositionStepBuilder<WatermarkBuilder> verticalSpacing(int spacing) {
        currentWatermark.setVerticalSpacing(spacing);
        return this;
    }

    public PositionStepBuilder<WatermarkBuilder> horizontalSpacing(int spacing) {
        currentWatermark.setHorizontalSpacing(spacing);
        return this;
    }

    public WatermarkBuilder end() {
        return (WatermarkBuilder) this;
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
        ValidationUtils.validateCurrentWatermark(currentWatermark);
        watermarks.add(currentWatermark);
        currentWatermark = new WatermarkAttributes();
        return (Service) this;
    }

    public Path apply(String directoryPath, String fileName) {
        ValidationUtils.validateDirectory(directoryPath);
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
