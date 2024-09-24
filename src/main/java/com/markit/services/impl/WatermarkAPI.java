package com.markit.services.impl;

import com.markit.exceptions.InvalidFileTypeException;
import com.markit.services.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.Executor;

/**
 * @author Oleg Cheban
 * @since 1.0
 */
public class WatermarkAPI implements WatermarkService.File, WatermarkService.Watermark {
    private static final Log logger = LogFactory.getLog(WatermarkAPI.class);
    private byte[] file;
    private FileType fileType;
    private String watermarkText;
    private Color color = Color.RED;
    private float dpi = 150f;
    private boolean trademark = false;
    private Executor executor;
    private boolean async = false;
    private WatermarkMethod watermarkMethod;
    private ImageWatermarker imageWatermarker;
    private PdfWatermarker pdfWatermarker;
    private OverlayPdfWatermarker overlayPdfWatermarker;
    private WatermarkPdfService watermarkPdfService;

    public WatermarkAPI() {
        this.imageWatermarker = new DefaultImageWatermarker();
        this.pdfWatermarker = new DefaultPdfWatermarker(this.imageWatermarker);
        this.overlayPdfWatermarker = new DefaultOverlayPdfWatermarker();
        this.watermarkPdfService = new DefaultWatermarkPdfService(this.pdfWatermarker, this.overlayPdfWatermarker);
    }

    public WatermarkAPI(Executor executor) {
        this.executor = executor;
        this.async = true;
        this.imageWatermarker = new DefaultImageWatermarker();
        this.pdfWatermarker = new DefaultPdfWatermarker(this.imageWatermarker);
        this.overlayPdfWatermarker = new DefaultOverlayPdfWatermarker();
        this.watermarkPdfService = new DefaultWatermarkPdfService(this.pdfWatermarker, this.overlayPdfWatermarker, this.executor);
    }

    public WatermarkAPI(Executor executor, ImageWatermarker w, PdfWatermarker d, OverlayPdfWatermarker o, WatermarkPdfService s) {
        this.executor = executor;
        this.async = true;
        this.imageWatermarker = w;
        this.pdfWatermarker = d;
        this.overlayPdfWatermarker = o;
        this.watermarkPdfService = s;
    }

    public WatermarkAPI(ImageWatermarker w, PdfWatermarker d, OverlayPdfWatermarker o, WatermarkPdfService s) {
        this.imageWatermarker = w;
        this.pdfWatermarker = d;
        this.overlayPdfWatermarker = o;
        this.watermarkPdfService = s;
    }

    @Override
    public WatermarkService.Watermark file(PDDocument document, FileType ft) throws IOException {
        this.file = convertPDDocumentToByteArray(document);
        this.fileType = ft;
        return this;
    }

    @Override
    public WatermarkService.Watermark file(byte[] f, FileType ft) {
        this.file = f;
        this.fileType = ft;
        return this;
    }

    @Override
    public WatermarkService.Watermark watermarkText(String text) {
        this.watermarkText = text;
        return this;
    }

    @Override
    public WatermarkService.Watermark watermarkMethod(WatermarkMethod method) {
        this.watermarkMethod = method;
        return this;
    }

    @Override
    public WatermarkService.Watermark color(Color c) {
        this.color = c;
        return this;
    }

    @Override
    public WatermarkService.Watermark dpi(float d) {
        this.dpi = d;
        return this;
    }

    @Override
    public WatermarkService.Watermark trademark() {
        this.trademark = true;
        return this;
    }

    @Override
    public WatermarkService.Watermark sync() {
        this.async = false;
        return this;
    }

    @Override
    public byte[] apply() throws IOException {
        switch (fileType){
            case PDF: return markPDF();
            case BMP:
            case JPEG:
            case PNG:
            case TIFF: return markImage();
            default: logger.error("undefined file type");
                throw new InvalidFileTypeException();
        }
    }

    private byte[] markPDF() throws IOException {
        return watermarkPdfService.watermark(file, async, watermarkText, color, dpi, trademark, watermarkMethod);
    }

    private byte[] markImage() throws IOException {
        return imageWatermarker.watermark(file, fileType, watermarkText, color, trademark);
    }


    private byte[] convertPDDocumentToByteArray(PDDocument document) throws IOException {
        try (var baos = new ByteArrayOutputStream()) {
            document.save(baos);
            return baos.toByteArray();
        }
    }
}
