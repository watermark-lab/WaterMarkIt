package com.markit.image;

import com.markit.api.FileType;
import com.markit.api.WatermarkAttributes;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author Oleg Cheban
 * @since 1.0
 */
public class DefaultImageWatermarker implements ImageWatermarker {
    private final ImageConverter imageConverter;
    private final WatermarkRenderer watermarkRenderer;
    private final WatermarkPositioner watermarkPositioner;
    public DefaultImageWatermarker() {
        this.imageConverter = new ImageConverter();
        this.watermarkRenderer = new WatermarkRenderer();
        this.watermarkPositioner = new WatermarkPositioner();
    }
    public DefaultImageWatermarker(ImageConverter imageConverter, WatermarkRenderer watermarkRenderer, WatermarkPositioner watermarkPositioner) {
        this.imageConverter = imageConverter;
        this.watermarkRenderer = watermarkRenderer;
        this.watermarkPositioner = watermarkPositioner;
    }

    @Override
    public byte[] watermark(byte[] sourceImageBytes, FileType fileType, List<WatermarkAttributes> attrs) throws IOException {
        if (imageConverter.isByteArrayEmpty(sourceImageBytes)) {
            return sourceImageBytes;
        }
        BufferedImage image = imageConverter.convertToBufferedImage(sourceImageBytes);
        return watermark(image, fileType, attrs);
    }

    @Override
    public byte[] watermark(File file, FileType fileType, List<WatermarkAttributes> attrs) throws IOException {
        BufferedImage image = ImageIO.read(file);
        return watermark(image, fileType, attrs);
    }

    public byte[] watermark(BufferedImage sourceImage, FileType fileType, List<WatermarkAttributes> attrs) throws IOException {
        var g2d = sourceImage.createGraphics();
        int imageWidth = sourceImage.getWidth();
        int imageHeight = sourceImage.getHeight();
        attrs.forEach(attr -> {
            int baseFontSize = watermarkRenderer.calculateFontSize(attr.getTextSize(), imageWidth, imageHeight);
            watermarkRenderer.drawWatermark(g2d, sourceImage, baseFontSize, attr, watermarkPositioner);
        });
        g2d.dispose();
        return imageConverter.convertToByteArray(sourceImage, fileType);
    }
}
