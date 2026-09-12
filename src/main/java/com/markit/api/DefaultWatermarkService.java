package com.markit.api;

import com.markit.api.formats.audio.AudioWatermarkContentStep;
import com.markit.api.formats.image.DefaultWatermarkImageBuilder;
import com.markit.api.formats.image.ImageWatermarkContentStep;
import com.markit.api.formats.pdf.DefaultWatermarkPDFBuilder;
import com.markit.api.formats.pdf.PdfWatermarkContentStep;
import com.markit.api.formats.video.DefaultWatermarkVideoBuilder;
import com.markit.api.formats.video.VideoWatermarkContentStep;
import com.markit.exceptions.InvalidPDFFileException;
import com.markit.api.formats.audio.DefaultAudioWatermarkBuilder;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executor;

/**
 * Main entry point for adding watermarks to various file formats.
 * Acts as a factory for format-specific watermark builders that provide
 * a fluent DSL for configuring and applying watermarks.
 *
 * @author Oleg Cheban
 * @since 1.0
 */
public class DefaultWatermarkService implements WatermarkService.FileFormatSelector {

    private final Executor executor;

    public DefaultWatermarkService() {
        this(null);
    }

    public DefaultWatermarkService(Executor executor) {
        this.executor = executor;
    }

    @Override
    public PdfWatermarkContentStep watermarkPDF(byte[] fileBytes) {
        try {
            return new DefaultWatermarkPDFBuilder(PDDocument.load(fileBytes), executor);
        } catch (IOException e) {
            throw new InvalidPDFFileException(e);
        }
    }

    @Override
    public PdfWatermarkContentStep watermarkPDF(File file) {
        try {
            return new DefaultWatermarkPDFBuilder(PDDocument.load(file), executor);
        } catch (IOException e) {
            throw new InvalidPDFFileException(e);
        }
    }

    @Override
    public PdfWatermarkContentStep watermarkPDF(PDDocument document) {
        return new DefaultWatermarkPDFBuilder(document, executor);
    }

    @Override
    public ImageWatermarkContentStep watermarkImage(File file) {
        return new DefaultWatermarkImageBuilder(file);
    }

    @Override
    public ImageWatermarkContentStep watermarkImage(byte[] fileBytes) {
        return new DefaultWatermarkImageBuilder(fileBytes);
    }

    public VideoWatermarkContentStep watermarkVideo(byte[] fileBytes) {
        return new DefaultWatermarkVideoBuilder(fileBytes);
    }

    @Override
    public VideoWatermarkContentStep watermarkVideo(File file) {
        return new DefaultWatermarkVideoBuilder(file);
    }

    @Override
    public AudioWatermarkContentStep watermarkAudio(byte[] fileBytes) {
        return new DefaultAudioWatermarkBuilder(fileBytes);
    }

    @Override
    public AudioWatermarkContentStep watermarkAudio(File file) {
        return new DefaultAudioWatermarkBuilder(file);
    }
}
