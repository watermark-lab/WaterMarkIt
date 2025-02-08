package com.markit.pdf;

import com.markit.image.ImageConverter;
import com.markit.image.ImageWatermarkerFactory;
import com.markit.pdf.draw.DefaultDrawPdfWatermarker;
import com.markit.pdf.overlay.*;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;

public class WatermarkPdfServiceBuilder {
    @NotNull
    public static WatermarkPdfService build(Executor executor) {
        var imageWatermarker = ImageWatermarkerFactory.getInstance().getService();

        //var imageWatermarker = new DefaultImageWatermarker();
        var drawPdfWatermarker = new DefaultDrawPdfWatermarker();

        var watermarkPositioner = new WatermarkPositioner();
        var imageBasedOverlayWatermarker = new ImageBasedOverlayWatermarker(watermarkPositioner);
        var textBasedOverlayWatermarker = new TextBasedOverlayWatermarker(new TrademarkHandler(), watermarkPositioner);
        var overlayPdfWatermarker = new DefaultPdfOverlayWatermarker(imageBasedOverlayWatermarker, textBasedOverlayWatermarker);

        return new DefaultWatermarkPdfService(drawPdfWatermarker, overlayPdfWatermarker, executor);
    }
}
