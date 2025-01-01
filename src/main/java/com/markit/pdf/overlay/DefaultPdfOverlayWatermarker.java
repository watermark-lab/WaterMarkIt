package com.markit.pdf.overlay;

import com.markit.api.WatermarkAttributes;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;
import org.apache.pdfbox.util.Matrix;

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
            attrs.stream()
                    .filter(WatermarkAttributes::getWatermarkEnabled)
                    .filter(attr -> attr.getPagePredicate().test(pageIndex))
                    .filter(attr -> attr.getDocumentPredicates().test(document))
                    .forEach(attr -> {
                        try {
                            overlay(contentStream, pageWidth, pageHeight, fontLoader.loadArialFont(document), attr);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }
    }

    private void overlay(PDPageContentStream contentStream, float pageWidth, float pageHeight, PDType0Font font, WatermarkAttributes attr) throws IOException {
        final int fontSize = attr.getSize() == 0 ? TEXT_SIZE : attr.getSize();
        contentStream.beginText();
        contentStream.setFont(PDType1Font.TIMES_BOLD, fontSize);
        contentStream.setNonStrokingColor(attr.getColor());
        contentStream.setGraphicsStateParameters(defineOpacity(attr.getOpacity()));
        float textWidth = font.getStringWidth(attr.getText()) / 1000 * fontSize;
        float textHeight = font.getFontDescriptor().getCapHeight() / 1000 * fontSize;
        var coordinates = positioner.defineXY(attr, (int) pageWidth, (int) pageHeight, (int) textWidth, (int) textHeight);
        float x = coordinates.get(0).getX() + textWidth / 2;
        float y = coordinates.get(0).getY() + textHeight / 2;

        contentStream.setTextMatrix(defineRotationMatrix(x, y, textWidth, textHeight, attr.getRotation()));
        contentStream.showText(attr.getText());
        contentStream.endText();

        if (attr.getTrademark()) {
            trademarkHandler.overlayTrademark(contentStream, attr, textWidth, textHeight, x, y, font, fontSize);
        }
    }

    private Matrix defineRotationMatrix(float centerX, float centerY, float textWidth, float textHeight, int rotation){
        var m = new Matrix();
        m.translate(centerX, centerY);
        m.rotate(Math.toRadians(rotation));
        m.translate(-textWidth / 2, -textHeight / 2); // Translate back to the original position
        return m;
    }

    private PDExtendedGraphicsState defineOpacity(float opacity) throws IOException {
        var transparencyState = new PDExtendedGraphicsState();
        transparencyState.setNonStrokingAlphaConstant(opacity);
        return transparencyState;
    }
}
