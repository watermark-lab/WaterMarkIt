package com.markit.api.impl.handlers;

import com.markit.pdf.WatermarkPdfServiceBuilder;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.util.concurrent.Executor;

/**
 * @author Oleg Cheban
 * @since 1.0
 */
public class WatermarkPdfHandler {
    public <T> WatermarkHandler getHandler(T file, Executor executor){
        var watermarkPdfService = WatermarkPdfServiceBuilder.build(executor);
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
