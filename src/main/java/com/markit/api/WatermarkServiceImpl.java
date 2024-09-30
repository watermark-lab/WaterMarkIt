package com.markit.api;

import com.markit.exceptions.UnsupportedFileTypeException;
import com.markit.exceptions.WatermarkingException;
import com.markit.image.DefaultImageWatermarker;
import com.markit.image.ImageWatermarker;
import com.markit.pdf.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executor;

/**
 * @author Oleg Cheban
 * @since 1.0
 */
public class WatermarkServiceImpl implements WatermarkService.File, WatermarkService.Watermark {
    private static final Log logger = LogFactory.getLog(WatermarkServiceImpl.class);
    private FileType fileType;
    private Executor executor;
    private boolean async;
    private ImageWatermarker imageWatermarker;
    private PdfWatermarker pdfWatermarker;
    private OverlayPdfWatermarker overlayPdfWatermarker;
    private WatermarkPdfService watermarkPdfService;
    private WatermarkHandler watermarkHandler;
    private WatermarkAttributes watermarkAttributes;

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
    public WatermarkService.Watermark file(PDDocument document) {
        return configureDefaultParams(FileType.PDF,
                () -> watermarkPdfService.watermark(document, async, watermarkAttributes)
        );
    }

    @Override
    public WatermarkService.Watermark file(byte[] fileBytes, FileType ft) {
        return configureDefaultParams(ft,
                (ft.equals(FileType.PDF)) ?
                        () -> watermarkPdfService.watermark(fileBytes, async, watermarkAttributes) :
                        () -> imageWatermarker.watermark(fileBytes, fileType, watermarkAttributes)
        );
    }

    @Override
    public WatermarkService.Watermark file(File file, FileType ft) {
        return configureDefaultParams(ft,
                (ft.equals(FileType.PDF)) ?
                        () -> watermarkPdfService.watermark(file, async, watermarkAttributes) :
                        () -> imageWatermarker.watermark(file, fileType, watermarkAttributes)
        );
    }

    private WatermarkService.Watermark configureDefaultParams(FileType ft, WatermarkHandler h) {
        watermarkAttributes = new WatermarkAttributes();
        watermarkAttributes.setMethod(defineMethodByFileType(ft));
        this.fileType = ft;
        this.watermarkHandler = h;
        return this;
    }

    @Override
    public WatermarkService.Watermark text(String text) {
        watermarkAttributes.setText(text);
        return this;
    }

    @Override
    public WatermarkService.Watermark textSize(int size) {
        watermarkAttributes.setTextSize(size);
        return this;
    }

    @Override
    public WatermarkService.Watermark method(WatermarkMethod method) {
        watermarkAttributes.setMethod(method);
        return this;
    }

    @Override
    public WatermarkService.Watermark position(WatermarkPosition position) {
        watermarkAttributes.setPosition(position);
        return this;
    }

    @Override
    public WatermarkService.Watermark color(Color c) {
        watermarkAttributes.setColor(c);
        return this;
    }

    @Override
    public WatermarkService.Watermark dpi(float d) {
        watermarkAttributes.setDpi(d);
        return this;
    }

    @Override
    public WatermarkService.Watermark trademark() {
        watermarkAttributes.setTrademark(true);
        return this;
    }

    @Override
    public WatermarkService.Watermark sync() {
        this.async = false;
        return this;
    }

    @Override
    public byte[] apply() {
        try {
            return this.watermarkHandler.apply();
        } catch (IOException e) {
            logger.error("Failed to watermark file", e);
            throw new WatermarkingException("Error watermarking the file", e);
        }
    }

    private WatermarkMethod defineMethodByFileType(FileType ft){
        switch (ft){
            case JPEG:
            case PNG:
            case TIFF:
            case BMP: return WatermarkMethod.DRAW;
            case PDF: return WatermarkMethod.OVERLAY;
            default: throw new UnsupportedFileTypeException("Unsupported file type: " + ft);
        }
    }
}
