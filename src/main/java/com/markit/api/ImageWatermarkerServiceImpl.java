package com.markit.api;

import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.util.concurrent.Executor;

/**
 * @author Oleg Cheban
 * @since 1.0
 */
public class ImageWatermarkerServiceImpl implements WatermarkService.WatermarkImageToFile, WatermarkService.ImageWatermarker {
    private Executor executor;

    public ImageWatermarkerServiceImpl() {
    }

    public ImageWatermarkerServiceImpl(Executor e) {
        this.executor = e;
    }

    @Override
    public WatermarkService.ImageWatermarker watermark(byte[] fileBytes, FileType fileType) {
        return null;
    }

    @Override
    public WatermarkService.ImageWatermarker watermark(File file, FileType fileType) {
        return null;
    }

    @Override
    public WatermarkService.ImageWatermarker watermark(PDDocument document) {
        return null;
    }

    @Override
    public WatermarkService.ImageWatermarker ofSize(int size) {
        return null;
    }

    @Override
    public WatermarkService.ImageWatermarker withOpacity(float opacity) {
        return null;
    }

    @Override
    public WatermarkService.ImageWatermarker withDpi(float dpi) {
        return null;
    }

    @Override
    public WatermarkService.ImageWatermarker and() {
        return null;
    }

    @Override
    public byte[] apply() {
        return new byte[0];
    }
}
