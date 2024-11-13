package com.markit.image;

import com.markit.api.FileType;
import com.markit.api.TextWatermarkAttributes;

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
    private final WatermarkPainter watermarkPainter;
    private final WatermarkPositioner watermarkPositioner;

    public DefaultImageWatermarker() {
        this.imageConverter = new ImageConverter();
        this.watermarkPainter = new WatermarkPainter();
        this.watermarkPositioner = new WatermarkPositioner();
    }

    public DefaultImageWatermarker(ImageConverter imageConverter, WatermarkPainter watermarkPainter, WatermarkPositioner watermarkPositioner) {
        this.imageConverter = imageConverter;
        this.watermarkPainter = watermarkPainter;
        this.watermarkPositioner = watermarkPositioner;
    }

    @Override
    public byte[] watermark(byte[] sourceImageBytes, FileType fileType, List<TextWatermarkAttributes> attrs) throws IOException {
        if (isByteArrayEmpty(sourceImageBytes)) {
            return sourceImageBytes;
        }
        BufferedImage image = imageConverter.convertToBufferedImage(sourceImageBytes);
        return watermark(image, fileType, attrs);
    }

    @Override
    public byte[] watermark(File file, FileType fileType, List<TextWatermarkAttributes> attrs) throws IOException {
        BufferedImage image = ImageIO.read(file);
        return watermark(image, fileType, attrs);
    }

    public byte[] watermark(BufferedImage sourceImage, FileType fileType, List<TextWatermarkAttributes> attrs) throws IOException {
        var g2d = sourceImage.createGraphics();
        int imageWidth = sourceImage.getWidth();
        int imageHeight = sourceImage.getHeight();
        attrs.forEach(attr -> {
            int baseFontSize = calculateFontSize(attr.getTextSize(), imageWidth, imageHeight);
            watermarkPainter.draw(g2d, sourceImage, baseFontSize, attr, watermarkPositioner);
        });
        g2d.dispose();
        return imageConverter.convertToByteArray(sourceImage, fileType);
    }

    public int calculateFontSize(int textSize, int imageWidth, int imageHeight) {
        if (textSize > 0) return textSize;
        return Math.min(imageWidth, imageHeight) / 10;
    }

    public boolean isByteArrayEmpty(byte[] byteArray) {
        return byteArray == null || byteArray.length == 0;
    }
}
