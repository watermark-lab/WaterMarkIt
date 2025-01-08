package com.markit.pdf.overlay;

import com.markit.api.WatermarkAttributes;
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
@SuppressWarnings("deprecation")
public class DefaultPdfOverlayWatermarker implements OverlayPdfWatermarker {
    private final ImageBasedOverlayWatermarker imageBasedOverlayWatermarker;
    private final TextBasedOverlayWatermarker textBasedOverlayWatermarker;

    public DefaultPdfOverlayWatermarker(ImageBasedOverlayWatermarker imageBasedOverlayWatermarker, TextBasedOverlayWatermarker textBasedOverlayWatermarker) {
        this.imageBasedOverlayWatermarker = imageBasedOverlayWatermarker;
        this.textBasedOverlayWatermarker = textBasedOverlayWatermarker;
    }

    @Override
    public void watermark(PDDocument document, int pageIndex, List<WatermarkAttributes> attrs) throws IOException {
        var page = document.getPage(pageIndex);
        var fontLoader = new FontLoader();
        try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true)) {
            attrs.forEach(attr -> {
                try {
                    contentStream.setGraphicsStateParameters(defineOpacity(attr.getOpacity()));
                    if (attr.getImage().isPresent()){
                        var image = LosslessFactory.createFromImage(document, attr.getImage().get());
                        imageBasedOverlayWatermarker.overlay(contentStream, image, page.getMediaBox(), attr);
                    } else {
                        textBasedOverlayWatermarker.overlay(contentStream, page.getMediaBox(), fontLoader.loadArialFont(document), attr);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    private PDExtendedGraphicsState defineOpacity(float opacity){
        var transparencyState = new PDExtendedGraphicsState();
        transparencyState.setNonStrokingAlphaConstant(opacity);
        return transparencyState;
    }
}
