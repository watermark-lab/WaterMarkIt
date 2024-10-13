package com.markit.image;

import com.markit.api.FileType;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author Oleg Cheban
 * @since 1.0
 */
public class ImageConverter {
    public BufferedImage convertToBufferedImage(byte[] imageBytes) throws IOException {
        return ImageIO.read(new ByteArrayInputStream(imageBytes));
    }

    public byte[] convertToByteArray(BufferedImage image, FileType fileType) throws IOException {
        var baos = new ByteArrayOutputStream();
        ImageIO.write(image, fileType.name(), baos);
        return baos.toByteArray();
    }
}
