package com.markit.pdf.overlay;

import com.markit.api.WatermarkAttributes;
import com.markit.servicelocator.ServiceFactory;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;

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
    public int getPriority() {
        return DEFAULT_PRIORITY;
    }

    @Override
    public void watermark(PDDocument document, int pageIndex, List<WatermarkAttributes> attrs) throws IOException {
        var imageBasedOverlayWatermarker =
                (ImageBasedOverlayWatermarker) ServiceFactory.getInstance()
                        .getService(ImageBasedOverlayWatermarker.class);
        var textBasedOverlayWatermarker =
                (TextBasedOverlayWatermarker) ServiceFactory.getInstance()
                        .getService(TextBasedOverlayWatermarker.class);
        var page = document.getPage(pageIndex);

        try (PDPageContentStream contentStream =
                     new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true)) {

            attrs.forEach(attr -> {
                try {
                    contentStream.setGraphicsStateParameters(setOpacity(attr.getOpacity()));
                    if (attr.getImage().isPresent()){
                        var image = LosslessFactory.createFromImage(document, attr.getImage().get());
                        imageBasedOverlayWatermarker.overlay(contentStream, image, page.getMediaBox(), attr);
                    } else {
                        textBasedOverlayWatermarker.overlay(contentStream, page.getMediaBox(), attr);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    private PDExtendedGraphicsState setOpacity(int opacity) {
        var transparencyState = new PDExtendedGraphicsState();
        transparencyState.setNonStrokingAlphaConstant((float) (opacity / 100.0));
        return transparencyState;
    }
}
