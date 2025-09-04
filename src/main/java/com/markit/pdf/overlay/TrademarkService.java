package com.markit.pdf.overlay;

import com.markit.api.WatermarkAttributes;
import com.markit.api.positioning.WatermarkPositionCoordinates;
import com.markit.servicelocator.Prioritizable;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.io.IOException;

/**
 * The interface for overlaying trademarks
 *
 * @author Oleg Cheban
 * @since 1.3.5
 */
public interface TrademarkService extends Prioritizable {

    /**
     * Adds a trademark
     *
     * @param contentStream pdf page content stream
     * @param attr a trademark has to check watermark attributes such as color, rotation, font, text, and size of text
     * @param c the watermark coordinates
     */
    void overlayTrademark(PDPageContentStream contentStream, WatermarkAttributes attr, WatermarkPositionCoordinates.Coordinates c) throws IOException;
}
