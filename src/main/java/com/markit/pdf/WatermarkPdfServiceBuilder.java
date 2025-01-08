package com.markit.pdf;

import com.markit.image.DefaultImageWatermarker;
import com.markit.image.ImageConverter;
import com.markit.pdf.draw.DefaultPdfDrawWatermarker;
import com.markit.pdf.overlay.*;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;

public class WatermarkPdfServiceBuilder {
    @NotNull
    public static WatermarkPdfService build(Executor executor) {
        var imageWatermarker = new DefaultImageWatermarker();
        var drawPdfWatermarker = new DefaultPdfDrawWatermarker(imageWatermarker, new ImageConverter());
        var watermarkPositioner = new WatermarkPositioner();
        var imageBasedOverlayWatermarker = new ImageBasedOverlayWatermarker(watermarkPositioner);
        var textBasedOverlayWatermarker = new TextBasedOverlayWatermarker(new TrademarkHandler(), watermarkPositioner);
        var overlayPdfWatermarker = new DefaultPdfOverlayWatermarker(imageBasedOverlayWatermarker, textBasedOverlayWatermarker);

        return new DefaultWatermarkPdfService(drawPdfWatermarker, overlayPdfWatermarker, executor);
    }
}
