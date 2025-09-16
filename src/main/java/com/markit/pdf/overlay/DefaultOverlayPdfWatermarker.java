package com.markit.pdf.overlay;

import com.markit.api.WatermarkAttributes;
import com.markit.pdf.overlay.font.DefaultFontProvider;
import com.markit.pdf.overlay.font.FontProvider;
import com.markit.servicelocator.ServiceFactory;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;

import java.io.IOException;
import java.util.List;

/**
 * @author Oleg Cheban
 * @since 1.0
 */
public class DefaultOverlayPdfWatermarker implements OverlayPdfWatermarker {

    public DefaultOverlayPdfWatermarker() {
    }

    @Override
    public void watermark(PDDocument document, int pageIndex, List<WatermarkAttributes> attrs) throws IOException {
        var imageBasedOverlayWatermarker = (ImageBasedOverlayWatermarker) ServiceFactory.getInstance()
                .getService(ImageBasedOverlayWatermarker.class);
        var textBasedOverlayWatermarker = (TextBasedOverlayWatermarker) ServiceFactory.getInstance()
                .getService(TextBasedOverlayWatermarker.class);
        var graphicsStateManager = (GraphicsStateManager) ServiceFactory.getInstance()
                .getService(GraphicsStateManager.class);

        var page = document.getPage(pageIndex);

        try (PDPageContentStream contentStream =
                     new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true)) {

            attrs.forEach(attr -> {
                try {
                    var opacityState = graphicsStateManager.createOpacityState(attr.getOpacity());
                    contentStream.setGraphicsStateParameters(opacityState);

                    if (attr.getImage().isPresent()){
                        var image = LosslessFactory.createFromImage(document, attr.getImage().get());
                        imageBasedOverlayWatermarker.overlay(contentStream, image, page.getMediaBox(), attr);
                    } else {
                        textBasedOverlayWatermarker.overlay(document, contentStream, page.getMediaBox(), attr);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    @Override
    public int getPriority() {
        return DEFAULT_PRIORITY;
    }
}
