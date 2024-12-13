package com.markit.api.handlers;

import com.markit.image.DefaultImageWatermarker;
import com.markit.image.ImageConverter;
import com.markit.pdf.DefaultWatermarkPdfService;
import com.markit.pdf.draw.DefaultPdfDrawWatermarker;
import com.markit.pdf.overlay.DefaultPdfOverlayWatermarker;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.util.concurrent.Executor;

/**
 * @author Oleg Cheban
 * @since 1.0
 */
public class WatermarkPdfHandler {
    public <T> WatermarkHandler getHandler(T file, Executor executor){
        var imageWatermarker = new DefaultImageWatermarker();
        var pdfWatermarker = new DefaultPdfDrawWatermarker(imageWatermarker, new ImageConverter());
        var overlayPdfWatermarker = new DefaultPdfOverlayWatermarker();
        var watermarkPdfService = new DefaultWatermarkPdfService(pdfWatermarker, overlayPdfWatermarker, executor);

        if (file instanceof PDDocument) {
            return (watermarks) -> watermarkPdfService.watermark((PDDocument) file, watermarks);
        } else if (file instanceof byte[]) {
            return (watermarks) -> watermarkPdfService.watermark((byte[]) file, watermarks);
        } else if (file instanceof File) {
            return (watermarks) -> watermarkPdfService.watermark((File) file, watermarks);
        }

        throw new IllegalArgumentException("Unsupported file type for watermarking.");
    }
}
