package com.markit.api;

import com.markit.api.image.WatermarkImageService;
import com.markit.api.image.DefaultWatermarkImageService;
import com.markit.api.pdf.WatermarkPDFService;
import com.markit.api.pdf.DefaultWatermarkPDFService;
import com.markit.exceptions.InvalidPDFFileException;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executor;

/**
 * @author Oleg Cheban
 * @since 1.0
 */
public class DefaultWatermarkService implements WatermarkService.WatermarkServiceSelector {
    private Executor executor;

    public DefaultWatermarkService() {
    }

    public DefaultWatermarkService(Executor e) {
        this.executor = e;
    }

    @Override
    public WatermarkPDFService watermarkPDF(byte[] fileBytes) {
        try {
            return new DefaultWatermarkPDFService(PDDocument.load(fileBytes), executor);
        } catch (IOException e) {
            throw new InvalidPDFFileException(e);
        }
    }

    @Override
    public WatermarkPDFService watermarkPDF(File file) {
        try {
            return new DefaultWatermarkPDFService(PDDocument.load(file), executor);
        } catch (IOException e) {
            throw new InvalidPDFFileException(e);
        }
    }

    @Override
    public WatermarkPDFService watermarkPDF(PDDocument document) {
        return new DefaultWatermarkPDFService(document, executor);
    }

    @Override
    public WatermarkImageService watermarkImage(byte[] fileBytes, ImageType imageType) {
        return new DefaultWatermarkImageService(fileBytes, imageType);
    }

    @Override
    public WatermarkImageService watermarkImage(File file, ImageType imageType) {
        return new DefaultWatermarkImageService(file, imageType);
    }
}
