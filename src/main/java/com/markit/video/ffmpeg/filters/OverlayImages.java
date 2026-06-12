package com.markit.video.ffmpeg.filters;

import javax.imageio.ImageIO;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Image transforms applied to a watermark before it is composited onto a video frame as an
 * ffmpeg overlay input. Shared by the text and image step builders.
 *
 * @author Oleg Cheban
 * @since 1.4.0
 */
final class OverlayImages {

    private OverlayImages() {
    }

    /**
     * Multiplies the image's alpha channel by the given opacity fraction, preserving any
     * existing transparency
     */
    static BufferedImage applyOpacity(BufferedImage image, float opacityFraction) {
        if (opacityFraction >= 1.0f) {
            return image;
        }

        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = result.createGraphics();
        try {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacityFraction));
            g2d.drawImage(image, 0, 0, null);
        } finally {
            g2d.dispose();
        }
        return result;
    }

    /**
     * Rotates the image around its center, growing the canvas to the rotated bounding box so no
     * content is clipped. The added corners stay transparent. The sign matches the image and PDF watermark painters
     */
    static BufferedImage rotate(BufferedImage image, int degrees) {
        if (degrees == 0) {
            return image;
        }

        double radians = Math.toRadians(degrees);
        double sin = Math.abs(Math.sin(radians));
        double cos = Math.abs(Math.cos(radians));
        int width = image.getWidth();
        int height = image.getHeight();
        int rotatedWidth = (int) Math.round(width * cos + height * sin);
        int rotatedHeight = (int) Math.round(width * sin + height * cos);

        BufferedImage rotated = new BufferedImage(rotatedWidth, rotatedHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = rotated.createGraphics();
        try {
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.translate((rotatedWidth - width) / 2.0, (rotatedHeight - height) / 2.0);
            g2d.rotate(-radians, width / 2.0, height / 2.0);
            g2d.drawImage(image, 0, 0, null);
        } finally {
            g2d.dispose();
        }
        return rotated;
    }

    /**
     * Writes the image to a temporary PNG so ffmpeg can consume it as an overlay input
     */
    static File writeTempPng(BufferedImage image) throws IOException {
        File tempFile = Files.createTempFile("wmk-img", ".png").toFile();
        ImageIO.write(image, "png", tempFile);
        return tempFile;
    }
}
