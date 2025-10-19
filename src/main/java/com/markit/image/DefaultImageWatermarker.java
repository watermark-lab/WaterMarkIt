package com.markit.image;

import com.markit.api.ImageType;
import com.markit.api.WatermarkAttributes;
import com.markit.servicelocator.ServiceFactory;
import com.markit.utils.ImageTypeDetector;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

/**
 * @author Oleg Cheban
 * @since 1.0
 */
public class DefaultImageWatermarker implements ImageWatermarker {
    private final ImageConverter imageConverter = new ImageConverter();

    @Override
    public byte[] watermark(byte[] sourceImageBytes, List<WatermarkAttributes> attrs) {
        if (isByteArrayEmpty(sourceImageBytes)) {
            return sourceImageBytes;
        }
        ImageType imageType = ImageTypeDetector.detect(sourceImageBytes);
        validateImageType(imageType);

        BufferedImage image = imageConverter.convertToBufferedImage(sourceImageBytes);
        return watermark(image, imageType, attrs);
    }

    @Override
    public byte[] watermark(File file, List<WatermarkAttributes> attrs) {
        ImageType imageType = ImageTypeDetector.detect(file);
        validateImageType(imageType);

        BufferedImage image = imageConverter.convertToBufferedImage(file);
        return watermark(image, imageType, attrs);
    }

    public byte[] watermark(BufferedImage sourceImage, ImageType imageType, List<WatermarkAttributes> attrs) {
        var g2d = sourceImage.createGraphics();

        attrs.forEach(attr -> {
            if (attr.getImage().isPresent()){
                var imagePainter = (ImageBasedWatermarkPainter) ServiceFactory.getInstance()
                        .getService(ImageBasedWatermarkPainter.class);
                imagePainter.draw(g2d, sourceImage, attr);
            } else {
                var textPainter = (TextBasedWatermarkPainter) ServiceFactory.getInstance()
                        .getService(TextBasedWatermarkPainter.class);
                textPainter.draw(g2d, sourceImage, attr);
            }
        });
        g2d.dispose();
        return imageConverter.convertToByteArray(sourceImage, imageType);
    }

    public boolean isByteArrayEmpty(byte[] byteArray) {
        return byteArray == null || byteArray.length == 0;
    }

    private void validateImageType(ImageType imageType) {
        if (!ImageIO.getImageWritersByFormatName(imageType.name().toLowerCase()).hasNext()) {
            throw new UnsupportedOperationException("No writer found for " + imageType);
        }
    }

    @Override
    public int getPriority() {
        return DEFAULT_PRIORITY;
    }
}
