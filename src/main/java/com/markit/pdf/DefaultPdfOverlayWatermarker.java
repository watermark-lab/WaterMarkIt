package com.markit.pdf;

import com.markit.api.WatermarkAttributes;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;

import java.io.IOException;
import java.util.List;

/**
 * @author Oleg Cheban
 * @since 1.0
 */
@SuppressWarnings("deprecation")
public class DefaultPdfOverlayWatermarker implements OverlayPdfWatermarker {
    private static final int TEXT_SIZE = 20;
    private final FontLoader fontLoader;
    private final WatermarkPositioner positioner;
    private final TrademarkHandler trademarkHandler;

    public DefaultPdfOverlayWatermarker() {
        this.fontLoader = new FontLoader();
        this.positioner = new WatermarkPositioner();
        this.trademarkHandler = new TrademarkHandler();
    }

    public DefaultPdfOverlayWatermarker(FontLoader fontLoader, WatermarkPositioner positioner, TrademarkHandler trademarkHandler) {
        this.fontLoader = fontLoader;
        this.positioner = positioner;
        this.trademarkHandler = trademarkHandler;
    }

    @Override
    public void watermark(PDDocument document, int pageIndex, List<WatermarkAttributes> attrs) throws IOException {
        PDPage page = document.getPage(pageIndex);
        PDRectangle mediaBox = page.getMediaBox();
        float pageWidth = mediaBox.getWidth();
        float pageHeight = mediaBox.getHeight();

        try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true)) {
            PDExtendedGraphicsState transparencyState = new PDExtendedGraphicsState();
            transparencyState.setNonStrokingAlphaConstant(0.5f);
            contentStream.setGraphicsStateParameters(transparencyState);

            attrs.forEach(attr -> {
                try {
                    overlayWatermark(contentStream, pageWidth, pageHeight, fontLoader.loadArialFont(document), attr);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    private void overlayWatermark(PDPageContentStream contentStream, float pageWidth, float pageHeight, PDType0Font font, WatermarkAttributes attr) throws IOException {
        final int fontSize = attr.getTextSize() == 0 ? TEXT_SIZE : attr.getTextSize();
        contentStream.beginText();
        contentStream.setFont(font, fontSize);
        contentStream.setNonStrokingColor(attr.getColor());

        float textWidth = font.getStringWidth(attr.getText()) / 1000 * fontSize;
        float textHeight = font.getFontDescriptor().getCapHeight() / 1000 * fontSize;

        var coordinates = positioner.defineXY(attr.getPosition(), (int) pageWidth, (int) pageHeight, (int) textWidth, (int) textHeight);

        contentStream.setTextTranslation(coordinates.getX(), coordinates.getY());
        contentStream.showText(attr.getText());
        contentStream.endText();

        if (attr.getTrademark()) {
            trademarkHandler.overlayTrademark(contentStream, attr.getTextSize(), textWidth, textHeight, coordinates.getX(), coordinates.getY(), font, attr.getColor());
        }
    }
}
