package com.markit.api;

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
    private String watermarkText;
    private int textSize;
    private Color color = Color.BLACK;
    private float dpi = 150f;
    private boolean trademark;
    private Executor executor;
    private boolean async;
    private WatermarkMethod watermarkMethod;
    private WatermarkPosition watermarkPosition;
    private ImageWatermarker imageWatermarker;
    private PdfWatermarker pdfWatermarker;
    private OverlayPdfWatermarker overlayPdfWatermarker;
    private WatermarkPdfService watermarkPdfService;
    private WatermarkHandler watermarkHandler;

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
                () -> watermarkPdfService.watermark(document, async, watermarkText, textSize, color, dpi, trademark, watermarkMethod, watermarkPosition)
        );
    }

    @Override
    public WatermarkService.Watermark file(byte[] f, FileType ft) {
        return configureDefaultParams(ft,
                (ft.equals(FileType.PDF)) ?
                        () -> watermarkPdfService.watermark(f, async, watermarkText, textSize, color, dpi, trademark, watermarkMethod, watermarkPosition) :
                        () -> imageWatermarker.watermark(f, fileType, watermarkText, textSize, color, trademark, watermarkPosition)
        );
    }

    @Override
    public WatermarkService.Watermark file(File f, FileType ft) {
        return configureDefaultParams(ft,
                (ft.equals(FileType.PDF)) ?
                        () -> watermarkPdfService.watermark(f, async, watermarkText, textSize, color, dpi, trademark, watermarkMethod, watermarkPosition) :
                        () -> imageWatermarker.watermark(f, fileType, watermarkText, textSize, color, trademark, watermarkPosition)
        );
    }

    private WatermarkService.Watermark configureDefaultParams(FileType ft, WatermarkHandler h) {
        this.fileType = ft;
        this.watermarkMethod = defaultMethod(ft);
        this.watermarkPosition = WatermarkPosition.CENTER;
        this.watermarkHandler = h;
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
    public byte[] apply() {
        try {
            return this.watermarkHandler.apply();
        } catch (IOException e) {
            logger.error("Failed to watermark file", e);
            throw new RuntimeException("Error watermarking the file", e);
        }
    }

    private WatermarkMethod defaultMethod(FileType ft){
        switch (ft){
            case JPEG:
            case PNG:
            case TIFF:
            case BMP: return WatermarkMethod.DRAW;
            case PDF: return WatermarkMethod.OVERLAY;
            default: throw new RuntimeException("undefined method");
        }
    }
}
