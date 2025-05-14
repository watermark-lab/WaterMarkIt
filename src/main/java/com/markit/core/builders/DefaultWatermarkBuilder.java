package com.markit.core.builders;

import com.markit.core.BaseWatermarkService;
import com.markit.core.WatermarkHandler;
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

    public DefaultWatermarkBuilder(WatermarkHandler watermarkHandler) {
        super(watermarkHandler);
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
        getWatermark().setImage(Optional.of(imageSupplier.get()));
        return builder();
    }

    public WatermarkBuilder size(int size) {
        getWatermark().setSize(size);
        return builder();
    }

    public WatermarkBuilder opacity(int opacity) {
        getWatermark().setOpacity(opacity);
        return builder();
    }

    public WatermarkBuilder rotation(int degree) {
        getWatermark().setRotationDegrees(degree);
        return builder();
    }

    public WatermarkBuilder enableIf(boolean condition) {
        getWatermark().setVisible(condition);
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
        getWatermark().setPosition(watermarkPosition);
        return this;
    }

    public PositionStepBuilder<WatermarkBuilder> adjust(int x, int y) {
        var adjustment = new WatermarkPositionCoordinates.Coordinates(x, y);
        getWatermark().setPositionAdjustment(adjustment);
        return this;
    }

    public PositionStepBuilder<WatermarkBuilder> verticalSpacing(int spacing) {
        getWatermark().setVerticalSpacing(spacing);
        return this;
    }

    public PositionStepBuilder<WatermarkBuilder> horizontalSpacing(int spacing) {
        getWatermark().setHorizontalSpacing(spacing);
        return this;
    }

    public TextBasedWatermarkBuilder<WatermarkBuilder> color(Color color) {
        Objects.requireNonNull(color);
        getWatermark().setColor(color);
        return this;
    }

    public TextBasedWatermarkBuilder<WatermarkBuilder> addTrademark() {
        getWatermark().setTrademark(true);
        return this;
    }

    public TextBasedWatermarkBuilder<WatermarkBuilder> withText(String text) {
        Objects.requireNonNull(text);
        getWatermark().setText(text);
        return this;
    }
}
