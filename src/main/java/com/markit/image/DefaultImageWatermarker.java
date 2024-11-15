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
    private final TextBasedWatermarkPainter textBasedWatermarkPainter;
    private final ImageBasedWatermarkPainter imageBasedWatermarkPainter;
    private final WatermarkPositioner watermarkPositioner;

    public DefaultImageWatermarker() {
        this.imageBasedWatermarkPainter = new ImageBasedWatermarkPainter();
        this.imageConverter = new ImageConverter();
        this.textBasedWatermarkPainter = new TextBasedWatermarkPainter();
        this.watermarkPositioner = new WatermarkPositioner();
    }

    public DefaultImageWatermarker(ImageConverter imageConverter, TextBasedWatermarkPainter textBasedWatermarkPainter, ImageBasedWatermarkPainter imageBasedWatermarkPainter, WatermarkPositioner watermarkPositioner) {
        this.imageConverter = imageConverter;
        this.textBasedWatermarkPainter = textBasedWatermarkPainter;
        this.imageBasedWatermarkPainter = imageBasedWatermarkPainter;
        this.watermarkPositioner = watermarkPositioner;
    }

    @Override
    public byte[] watermark(byte[] sourceImageBytes, FileType fileType, List<WatermarkAttributes> attrs) throws IOException {
        if (isByteArrayEmpty(sourceImageBytes)) {
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
            if (attr.getImage().isPresent()){
                imageBasedWatermarkPainter.draw(g2d, sourceImage, attr);
            } else {
                textBasedWatermarkPainter.draw(g2d, sourceImage, calculateFontSize(attr.getSize(), imageWidth, imageHeight), attr, watermarkPositioner);
            }
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
