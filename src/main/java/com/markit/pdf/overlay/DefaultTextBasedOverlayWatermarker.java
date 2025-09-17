package com.markit.pdf.overlay;

import com.markit.api.WatermarkAttributes;
import com.markit.api.positioning.WatermarkPositionCoordinates;
import com.markit.pdf.overlay.font.DefaultFontProvider;
import com.markit.pdf.overlay.font.FontProvider;
import com.markit.servicelocator.ServiceFactory;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

import java.io.IOException;

public class DefaultTextBasedOverlayWatermarker implements TextBasedOverlayWatermarker {

    @Override
    public void overlay(PDDocument document, PDPageContentStream contentStream, PDRectangle pdRectangle, WatermarkAttributes attr) throws IOException {
        initFonts(document, contentStream, attr);

        var coordinates = WatermarkPositioner.defineXY(attr,
                (int) pdRectangle.getWidth(), (int) pdRectangle.getHeight (),
                (int) attr.getPdfWatermarkTextWidth(), (int) attr.getPdfWatermarkTextHeight());

        for (WatermarkPositionCoordinates.Coordinates c : coordinates) {
            var textTransformationProvider = (TextMatrixTransformationProvider) ServiceFactory.getInstance()
                    .getService(TextMatrixTransformationProvider.class);

            var matrix = textTransformationProvider.createTextRotationMatrix(
                    c, attr.getPdfWatermarkTextWidth(), attr.getPdfWatermarkTextHeight(), attr.getRotationDegrees());

            contentStream.beginText();
            contentStream.setFont(attr.getResolvedPdfFont(), attr.getPdfTextSize());
            contentStream.setNonStrokingColor(attr.getColor());
            contentStream.setTextMatrix(matrix);
            contentStream.showText(attr.getText());
            contentStream.endText();

            if (attr.getTrademark()) {
                var trademarkService = (TrademarkService) ServiceFactory.getInstance().getService(TrademarkService.class);
                trademarkService.overlayTrademark(contentStream, attr, c);
            }
        }
    }

    private void initFonts(PDDocument document, PDPageContentStream contentStream, WatermarkAttributes attr) throws IOException {
        var fontProvider = (DefaultFontProvider) ServiceFactory.getInstance().getService(FontProvider.class);
        if (fontProvider.canHandle(attr)) {
            contentStream.setFont(fontProvider.loadFont(document, attr), attr.getPdfTextSize());
        }
    }

    @Override
    public int getPriority() {
        return DEFAULT_PRIORITY;
    }
}
