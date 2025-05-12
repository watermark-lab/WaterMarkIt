package com.markit.core;

import com.markit.core.formats.audio.DefaultWatermarkAudioService;
import com.markit.core.formats.audio.WatermarkAudioService;
import com.markit.core.formats.image.DefaultWatermarkImageBuilder;
import com.markit.core.formats.image.WatermarkImageService;
import com.markit.core.formats.pdf.DefaultWatermarkPDFBuilder;
import com.markit.core.formats.pdf.WatermarkPDFService;
import com.markit.exceptions.InvalidPDFFileException;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executor;

/**
 * @author Oleg Cheban
 * @since 1.0
 */
public class DefaultWatermarkService implements WatermarkService.FileFormatSelector {

    private Executor executor;

    public DefaultWatermarkService() {
    }

    public DefaultWatermarkService(Executor e) {
        this.executor = e;
    }

    @Override
    public WatermarkPDFService watermarkPDF(byte[] fileBytes) {
        try {
            return new DefaultWatermarkPDFBuilder(PDDocument.load(fileBytes), executor);
        } catch (IOException e) {
            throw new InvalidPDFFileException(e);
        }
    }

    @Override
    public WatermarkPDFService watermarkPDF(File file) {
        try {
            return new DefaultWatermarkPDFBuilder(PDDocument.load(file), executor);
        } catch (IOException e) {
            throw new InvalidPDFFileException(e);
        }
    }

    @Override
    public WatermarkPDFService watermarkPDF(PDDocument document) {
        return new DefaultWatermarkPDFBuilder(document, executor);
    }

    @Override
    public WatermarkImageService watermarkImage(byte[] fileBytes, ImageType imageType) {
        return new DefaultWatermarkImageBuilder(fileBytes, imageType);
    }

    @Override
    public WatermarkImageService watermarkImage(File file, ImageType imageType) {
        return new DefaultWatermarkImageBuilder(file, imageType);
    }

    @Override
    public WatermarkAudioService watermarkAudio(File file) {
        return new DefaultWatermarkAudioService(file);
    }

}
