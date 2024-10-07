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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * @author Oleg Cheban
 * @since 1.0
 */
public class WatermarkServiceImpl implements WatermarkService.File, WatermarkService.Watermark {
    private static final Log logger = LogFactory.getLog(WatermarkServiceImpl.class);
    private FileType fileType;
    private boolean async;
    private ImageWatermarker imageWatermarker;
    private PdfWatermarker pdfWatermarker;
    private OverlayPdfWatermarker overlayPdfWatermarker;
    private WatermarkPdfService watermarkPdfService;
    private WatermarkHandler watermarkHandler;
    private List<WatermarkAttributes> watermarks;
    private WatermarkAttributes currentWatermark;

    public WatermarkServiceImpl() {
        this.imageWatermarker = new DefaultImageWatermarker();
        this.pdfWatermarker = new DefaultPdfDrawWatermarker(this.imageWatermarker);
        this.overlayPdfWatermarker = new DefaultPdfOverlayWatermarker();
        this.watermarkPdfService = new DefaultWatermarkPdfService(this.pdfWatermarker, this.overlayPdfWatermarker);
        this.watermarks = new ArrayList<>();
    }

    public WatermarkServiceImpl(Executor executor) {
        this.async = true;
        this.imageWatermarker = new DefaultImageWatermarker();
        this.pdfWatermarker = new DefaultPdfDrawWatermarker(this.imageWatermarker);
        this.overlayPdfWatermarker = new DefaultPdfOverlayWatermarker();
        this.watermarkPdfService = new DefaultWatermarkPdfService(this.pdfWatermarker, this.overlayPdfWatermarker, executor);
        this.watermarks = new ArrayList<>();
    }

    public WatermarkServiceImpl(ImageWatermarker w, PdfWatermarker d, OverlayPdfWatermarker o, WatermarkPdfService s) {
        this.imageWatermarker = w;
        this.pdfWatermarker = d;
        this.overlayPdfWatermarker = o;
        this.watermarkPdfService = s;
        this.watermarks = new ArrayList<>();
    }

    @Override
    public WatermarkService.Watermark watermark(PDDocument document) {
        return configureDefaultParams(FileType.PDF,
                () -> watermarkPdfService.watermark(document, async, watermarks)
        );
    }

    @Override
    public WatermarkService.Watermark watermark(byte[] fileBytes, FileType ft) {
        return configureDefaultParams(ft,
                (ft.equals(FileType.PDF)) ?
                        () -> watermarkPdfService.watermark(fileBytes, async, watermarks) :
                        () -> imageWatermarker.watermark(fileBytes, fileType, watermarks)
        );
    }

    @Override
    public WatermarkService.Watermark watermark(File file, FileType ft) {
        return configureDefaultParams(ft,
                (ft.equals(FileType.PDF)) ?
                        () -> watermarkPdfService.watermark(file, async, watermarks) :
                        () -> imageWatermarker.watermark(file, fileType, watermarks)
        );
    }

    private WatermarkService.Watermark configureDefaultParams(FileType ft, WatermarkHandler h) {
        currentWatermark = new WatermarkAttributes();
        currentWatermark.setMethod(defineMethodByFileType(ft));
        this.fileType = ft;
        this.watermarkHandler = h;
        return this;
    }

    @Override
    public WatermarkService.Watermark withText(String text) {
        currentWatermark.setText(text);
        return this;
    }

    @Override
    public WatermarkService.Watermark ofSize(int size) {
        currentWatermark.setTextSize(size);
        return this;
    }

    @Override
    public WatermarkService.Watermark usingMethod(WatermarkMethod method) {
        currentWatermark.setMethod(method);
        return this;
    }

    @Override
    public WatermarkService.Watermark atPosition(WatermarkPosition position) {
        currentWatermark.setPosition(position);
        return this;
    }

    @Override
    public WatermarkService.Watermark inColor(Color c) {
        currentWatermark.setColor(c);
        return this;
    }

    @Override
    public WatermarkService.Watermark withDpi(float d) {
        currentWatermark.setDpi(d);
        return this;
    }

    @Override
    public WatermarkService.Watermark asTrademark() {
        currentWatermark.setTrademark(true);
        return this;
    }

    @Override
    public WatermarkService.Watermark sync() {
        this.async = false;
        return this;
    }

    @Override
    public WatermarkService.Watermark and() {
        // If no watermark has been configured after and(), ignore it.
        // text is absolutely essential for watermarking
        if (!currentWatermark.getText().isEmpty()) {
            watermarks.add(currentWatermark);
            currentWatermark = new WatermarkAttributes();
            currentWatermark.setMethod(defineMethodByFileType(fileType));
        } else {
            throw new IllegalStateException("Each .and() must be followed by a watermark configuration before calling .and() again.");
        }
        return this;
    }

    @Override
    public byte[] apply() {
        try {
            and();
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
