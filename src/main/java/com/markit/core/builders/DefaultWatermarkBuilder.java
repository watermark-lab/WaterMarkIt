package com.markit.core.builders;

import com.markit.core.BaseWatermarkService;
import com.markit.core.WatermarkAttributes;
import com.markit.core.positioning.WatermarkPosition;
import com.markit.core.positioning.WatermarkPositionCoordinates;
import com.markit.image.ImageConverter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * @author Oleg Cheban
 * @since 1.3.0
 */
public class DefaultWatermarkBuilder<WatermarkService, WatermarkBuilder> extends BaseWatermarkService<WatermarkService>
        implements PositionStepBuilder<WatermarkBuilder>, TextBasedWatermarkBuilder<WatermarkBuilder> {

    public DefaultWatermarkBuilder() {
        this.currentWatermark = new WatermarkAttributes();
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
        return builder();
    }

    public WatermarkBuilder size(int size) {
        currentWatermark.setSize(size);
        return builder();
    }

    public WatermarkBuilder opacity(int opacity) {
        currentWatermark.setOpacity(opacity);
        return builder();
    }

    public WatermarkBuilder rotation(int degree) {
        currentWatermark.setRotationDegrees(degree);
        return builder();
    }

    public WatermarkBuilder enableIf(boolean condition) {
        currentWatermark.setVisible(condition);
        return builder();
    }

    public WatermarkBuilder end() {
        return builder();
    }

    private WatermarkBuilder builder() {
        @SuppressWarnings("unchecked")
        var result = (WatermarkBuilder) this;
        return result;
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

    public TextBasedWatermarkBuilder<WatermarkBuilder> color(Color color) {
        Objects.requireNonNull(color);
        currentWatermark.setColor(color);
        return this;
    }

    public TextBasedWatermarkBuilder<WatermarkBuilder> addTrademark() {
        currentWatermark.setTrademark(true);
        return this;
    }

    public TextBasedWatermarkBuilder<WatermarkBuilder> withText(String text) {
        Objects.requireNonNull(text);
        currentWatermark.setText(text);
        return this;
    }
}
