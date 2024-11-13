package com.markit.api;

import com.markit.api.handlers.WatermarkHandler;
import com.markit.api.handlers.WatermarksHandler;
import com.markit.exceptions.UnsupportedFileTypeException;
import com.markit.exceptions.WatermarkingException;
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
public class TextBasedWatermarkServiceImpl implements WatermarkService.TextBasedWatermarker, WatermarkService.TextBasedWatermarkBuilder {
    private static final Log logger = LogFactory.getLog(TextBasedWatermarkServiceImpl.class);
    private FileType fileType;
    private WatermarkHandler watermarkHandler;
    private final List<TextWatermarkAttributes> watermarks = new ArrayList<>();
    private TextWatermarkAttributes currentWatermark;
    private Executor executor;

    public TextBasedWatermarkServiceImpl() {
    }

    public TextBasedWatermarkServiceImpl(Executor e) {
        this.executor = e;
    }

    @Override
    public WatermarkService.TextBasedWatermarkBuilder watermark(PDDocument document) {
        return configureDefaultParams(FileType.PDF, new WatermarksHandler().getHandler(document, FileType.PDF, this.executor));
    }

    @Override
    public WatermarkService.TextBasedWatermarkBuilder watermark(byte[] fileBytes, FileType ft) {
        return configureDefaultParams(ft, new WatermarksHandler().getHandler(fileBytes, ft, this.executor));
    }

    @Override
    public WatermarkService.TextBasedWatermarkBuilder watermark(File file, FileType ft) {
        return configureDefaultParams(ft, new WatermarksHandler().getHandler(file, ft, this.executor));
    }

    private WatermarkService.TextBasedWatermarkBuilder configureDefaultParams(FileType ft, WatermarkHandler h) {
        currentWatermark = new TextWatermarkAttributes();
        currentWatermark.setMethod(defineMethodByFileType(ft));
        this.fileType = ft;
        this.watermarkHandler = h;
        return this;
    }

    @Override
    public WatermarkService.TextBasedWatermarkBuilder withText(String text) {
        currentWatermark.setText(text);
        return this;
    }

    @Override
    public WatermarkService.TextBasedWatermarkBuilder ofSize(int size) {
        currentWatermark.setTextSize(size);
        return this;
    }

    @Override
    public WatermarkService.TextBasedWatermarkBuilder usingMethod(WatermarkMethod method) {
        currentWatermark.setMethod(method);
        return this;
    }

    @Override
    public WatermarkService.TextBasedWatermarkBuilder atPosition(WatermarkPosition position) {
        currentWatermark.setPosition(position);
        return this;
    }

    @Override
    public WatermarkService.TextBasedWatermarkBuilder inColor(Color c) {
        currentWatermark.setColor(c);
        return this;
    }

    @Override
    public WatermarkService.TextBasedWatermarkBuilder withOpacity(float opacity) {
        currentWatermark.setOpacity(opacity);
        return this;
    }

    @Override
    public WatermarkService.TextBasedWatermarkBuilder withDpi(float d) {
        currentWatermark.setDpi(d);
        return this;
    }

    @Override
    public WatermarkService.TextBasedWatermarkBuilder withTrademark() {
        currentWatermark.setTrademark(true);
        return this;
    }

    @Override
    public WatermarkService.TextBasedWatermarkBuilder rotate(int degree) {
        currentWatermark.setRotation(degree);
        return this;
    }

    @Override
    public WatermarkService.TextBasedWatermarkBuilder and() {
        if (!currentWatermark.getText().isEmpty()) {
            watermarks.add(currentWatermark);
            currentWatermark = new TextWatermarkAttributes();
            currentWatermark.setMethod(defineMethodByFileType(fileType));
        }
        return this;
    }

    @Override
    public byte[] apply() {
        try {
            and();
            return this.watermarkHandler.apply(this.watermarks);
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
