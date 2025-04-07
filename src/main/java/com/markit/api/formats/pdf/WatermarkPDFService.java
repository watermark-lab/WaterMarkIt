package com.markit.api.formats.pdf;

import com.markit.api.builders.TextBasedWatermarkBuilder;
import com.markit.api.WatermarkingMethod;
import com.markit.api.builders.WatermarkBuilder;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.function.Predicate;

/**
 * Watermark Service for applying watermarks to PDF files
 *
 * @author Oleg Cheban
 * @since 1.3.0
 */
public interface WatermarkPDFService {

    /**
     * Sets the text to be used as the watermark
     *
     * @param text The text for the watermark
     */
    TextBasedWatermarkBuilder<WatermarkPDFBuilder> withText(String text);

    /**
     * Sets the image to be used as the watermark
     */
    WatermarkPDFBuilder withImage(byte[] image);
    WatermarkPDFBuilder withImage(BufferedImage image);
    WatermarkPDFBuilder withImage(File image);

    /**
     * The general pdf watermarks builder
     */
    interface WatermarkPDFBuilder extends WatermarkBuilder<WatermarkPDFService, WatermarkPDFBuilder> {

        /**
         * Defines the method for adding a watermark (default is DRAW)
         *
         * @param watermarkingMethod The method to use for watermarking
         * @see WatermarkingMethod
         */
        WatermarkPDFBuilder method(WatermarkingMethod watermarkingMethod);

        /**
         * Sets the dpi of the watermark
         */
        WatermarkPDFBuilder dpi(int dpi);

        /**
         * Adds a condition to filter the document when applying the watermark
         * Only documents that meet the condition will have the watermark applied
         *
         * @param predicate: A condition that takes a PDDocument as input and returns true/false
         * @see Predicate
         */
        WatermarkPDFBuilder documentFilter(Predicate<PDDocument> predicate);

        /**
         * Adds a condition to filter the page when applying the watermark
         * Only pages that meet the condition will have the watermark applied
         *
         * @param predicate A condition that takes a page number (Integer) as input and returns true/false. The page index starts from 0
         * @see Predicate
         */
        WatermarkPDFBuilder pageFilter(Predicate<Integer> predicate);
    }
}
