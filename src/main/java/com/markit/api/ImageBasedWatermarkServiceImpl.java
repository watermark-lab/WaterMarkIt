package com.markit.api;

import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.util.concurrent.Executor;

/**
 * @author Oleg Cheban
 * @since 1.0
 */
public class ImageBasedWatermarkServiceImpl implements WatermarkService.ImageBasedWatermarker, WatermarkService.ImageBasedWatermarkBuilder {
    private Executor executor;

    public ImageBasedWatermarkServiceImpl() {
    }

    public ImageBasedWatermarkServiceImpl(Executor e) {
        this.executor = e;
    }

    @Override
    public WatermarkService.ImageBasedWatermarkBuilder watermark(byte[] fileBytes, FileType fileType) {
        return null;
    }

    @Override
    public WatermarkService.ImageBasedWatermarkBuilder watermark(File file, FileType fileType) {
        return null;
    }

    @Override
    public WatermarkService.ImageBasedWatermarkBuilder watermark(PDDocument document) {
        return null;
    }

    @Override
    public WatermarkService.ImageBasedWatermarkBuilder ofSize(int size) {
        return null;
    }

    @Override
    public WatermarkService.ImageBasedWatermarkBuilder withOpacity(float opacity) {
        return null;
    }

    @Override
    public WatermarkService.ImageBasedWatermarkBuilder withDpi(float dpi) {
        return null;
    }

    @Override
    public WatermarkService.ImageBasedWatermarkBuilder and() {
        return null;
    }

    @Override
    public byte[] apply() {
        return new byte[0];
    }
}
