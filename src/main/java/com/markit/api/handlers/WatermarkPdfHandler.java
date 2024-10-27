package com.markit.api.handlers;

import com.markit.api.FileType;
import com.markit.image.DefaultImageWatermarker;
import com.markit.image.ImageWatermarker;
import com.markit.pdf.DefaultWatermarkPdfService;
import com.markit.pdf.WatermarkPdfService;
import com.markit.pdf.draw.DefaultPdfDrawWatermarker;
import com.markit.pdf.draw.PdfWatermarker;
import com.markit.pdf.overlay.DefaultPdfOverlayWatermarker;
import com.markit.pdf.overlay.OverlayPdfWatermarker;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.util.concurrent.Executor;

/**
 * @author Oleg Cheban
 * @since 1.0
 */
public class WatermarkPdfHandler {
    private ImageWatermarker imageWatermarker;
    private PdfWatermarker pdfWatermarker;
    private OverlayPdfWatermarker overlayPdfWatermarker;
    private WatermarkPdfService watermarkPdfService;

    public <T> WatermarkHandler getHandler(T file, FileType fileType, Executor executor){
        this.imageWatermarker = new DefaultImageWatermarker();
        this.pdfWatermarker = new DefaultPdfDrawWatermarker(this.imageWatermarker);
        this.overlayPdfWatermarker = new DefaultPdfOverlayWatermarker();
        this.watermarkPdfService = new DefaultWatermarkPdfService(this.pdfWatermarker, this.overlayPdfWatermarker, executor);

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
