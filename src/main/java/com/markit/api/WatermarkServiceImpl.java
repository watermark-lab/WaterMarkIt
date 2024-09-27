package com.markit.api;

import com.markit.exceptions.InvalidFileTypeException;
import com.markit.image.DefaultImageWatermarker;
import com.markit.image.ImageWatermarker;
import com.markit.pdf.*;
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
public class WatermarkServiceImpl implements WatermarkService.File, WatermarkService.Watermark {
    private static final Log logger = LogFactory.getLog(WatermarkServiceImpl.class);
    private byte[] file;
    private FileType fileType;
    private String watermarkText;
    private int textSize;
    private Color color = Color.BLACK;
    private float dpi = 150f;
    private boolean trademark;
    private Executor executor;
    private boolean async;
    private WatermarkMethod watermarkMethod;
    private WatermarkPosition watermarkPosition = WatermarkPosition.CENTER;
    private ImageWatermarker imageWatermarker;
    private PdfWatermarker pdfWatermarker;
    private OverlayPdfWatermarker overlayPdfWatermarker;
    private WatermarkPdfService watermarkPdfService;

    public WatermarkServiceImpl() {
        this.imageWatermarker = new DefaultImageWatermarker();
        this.pdfWatermarker = new DefaultPdfDrawWatermarker(this.imageWatermarker);
        this.overlayPdfWatermarker = new DefaultPdfOverlayWatermarker();
        this.watermarkPdfService = new DefaultWatermarkPdfService(this.pdfWatermarker, this.overlayPdfWatermarker);
    }

    public WatermarkServiceImpl(Executor executor) {
        this.executor = executor;
        this.async = true;
        this.imageWatermarker = new DefaultImageWatermarker();
        this.pdfWatermarker = new DefaultPdfDrawWatermarker(this.imageWatermarker);
        this.overlayPdfWatermarker = new DefaultPdfOverlayWatermarker();
        this.watermarkPdfService = new DefaultWatermarkPdfService(this.pdfWatermarker, this.overlayPdfWatermarker, this.executor);
    }

    public WatermarkServiceImpl(Executor executor, ImageWatermarker w, PdfWatermarker d, OverlayPdfWatermarker o, WatermarkPdfService s) {
        this.executor = executor;
        this.async = true;
        this.imageWatermarker = w;
        this.pdfWatermarker = d;
        this.overlayPdfWatermarker = o;
        this.watermarkPdfService = s;
    }

    public WatermarkServiceImpl(ImageWatermarker w, PdfWatermarker d, OverlayPdfWatermarker o, WatermarkPdfService s) {
        this.imageWatermarker = w;
        this.pdfWatermarker = d;
        this.overlayPdfWatermarker = o;
        this.watermarkPdfService = s;
    }

    @Override
    public WatermarkService.Watermark file(PDDocument document, FileType ft) throws IOException {
        this.file = convertPDDocumentToByteArray(document);
        this.fileType = ft;
        this.watermarkMethod = defineMethod(ft);
        return this;
    }

    @Override
    public WatermarkService.Watermark file(byte[] f, FileType ft) {
        this.file = f;
        this.fileType = ft;
        this.watermarkMethod = defineMethod(ft);
        return this;
    }

    @Override
    public WatermarkService.Watermark text(String text) {
        this.watermarkText = text;
        return this;
    }

    @Override
    public WatermarkService.Watermark textSize(int size) {
        this.textSize = size;
        return this;
    }

    @Override
    public WatermarkService.Watermark method(WatermarkMethod method) {
        this.watermarkMethod = method;
        return this;
    }

    @Override
    public WatermarkService.Watermark position(WatermarkPosition watermarkPosition) {
        this.watermarkPosition = watermarkPosition;
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
        return watermarkPdfService.watermark(file, async, watermarkText, textSize, color, dpi, trademark, watermarkMethod, watermarkPosition);
    }

    private byte[] markImage() throws IOException {
        return imageWatermarker.watermark(file, fileType, watermarkText, textSize, color, trademark, watermarkPosition);
    }

    private WatermarkMethod defineMethod(FileType ft){
        switch (ft){
            case JPEG:
            case PNG:
            case TIFF:
            case BMP: return WatermarkMethod.DRAW;
            case PDF: return WatermarkMethod.OVERLAY;
            default: throw new RuntimeException("undefined method");
        }
    }

    private byte[] convertPDDocumentToByteArray(PDDocument document) throws IOException {
        try (var baos = new ByteArrayOutputStream()) {
            document.save(baos);
            return baos.toByteArray();
        }
    }
}
