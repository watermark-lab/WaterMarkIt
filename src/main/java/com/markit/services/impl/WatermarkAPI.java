package com.markit.services.impl;

import com.markit.exceptions.InvalidFileTypeException;
import com.markit.services.WatermarkService;
import com.markit.services.PdfWatermarker;
import com.markit.services.ImageWatermarker;
import com.markit.services.WatermarkPdfService;
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
public class WatermarkAPI implements WatermarkService {
    private static final Log logger = LogFactory.getLog(WatermarkAPI.class);

    private byte[] file;

    private FileType fileType;

    private String watermarkText;

    private Color watermarkColor = new Color(22, 112, 203);

    private float dpi = 150;

    private Boolean async = false;

    private Boolean trademark = false;

    private Executor executor;

    private ImageWatermarker imageWatermarker;

    private PdfWatermarker pdfWatermarker;

    private WatermarkPdfService watermarkPdfService;

    public WatermarkAPI() {
        this.imageWatermarker = new DefaultImageWatermarker();
        this.pdfWatermarker = new DefaultPdfWatermarker(this.imageWatermarker);
        this.watermarkPdfService = new DefaultWatermarkPdfService(this.pdfWatermarker);
    }

    public WatermarkAPI(Executor e) {
        this.executor = e;
        this.async = true;
        this.imageWatermarker = new DefaultImageWatermarker();
        this.pdfWatermarker = new DefaultPdfWatermarker(this.imageWatermarker);
        this.watermarkPdfService = new DefaultWatermarkPdfService(this.pdfWatermarker, this.executor);
    }

    public WatermarkAPI file(byte[] fl) {
        this.file = fl;
        return this;
    }

    @Override
    public WatermarkService file(PDDocument pdDocument) throws IOException {
        this.file = convertPDDocumentToByteArray(pdDocument);
        return this;
    }

    public WatermarkAPI fileType(FileType ct) {
        this.fileType = ct;
        return this;
    }

    public WatermarkAPI watermarkText(String text) {
        this.watermarkText = text;
        return this;
    }

    public WatermarkAPI setWatermarkImage(ImageWatermarker s) {
        this.imageWatermarker = s;
        return this;
    }

    public WatermarkAPI setWatermarkPdf(PdfWatermarker s) {
        if (this.imageWatermarker == null) throw new RuntimeException("WatermarkImage isn't initialized");
        this.pdfWatermarker = s;
        return this;
    }

    public WatermarkAPI setWatermarkPdfService(WatermarkPdfService s) {
        if (this.pdfWatermarker == null) throw new RuntimeException("WatermarkPdf isn't initialized");
        this.watermarkPdfService = s;
        return this;
    }

    public WatermarkAPI dpi(float d) {
        this.dpi = d;
        return this;
    }

    @Override
    public WatermarkService sync() {
        this.async = false;
        return this;
    }

    @Override
    public WatermarkService color(Color c) {
        this.watermarkColor = c;
        return this;
    }

    @Override
    public WatermarkService trademark() {
        this.trademark = true;
        return this;
    }

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
        return watermarkPdfService.watermark(file, async, watermarkText, watermarkColor, dpi, trademark);
    }

    private byte[] markImage() throws IOException {
        return imageWatermarker.watermark(file, fileType, watermarkText, watermarkColor, trademark);
    }

    private byte[] convertPDDocumentToByteArray(PDDocument document) throws IOException {
        try (var baos = new ByteArrayOutputStream()) {
            document.save(baos);
            return baos.toByteArray();
        }
    }
}
