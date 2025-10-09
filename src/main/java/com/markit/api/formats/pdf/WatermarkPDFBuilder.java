package com.markit.api.formats.pdf;

import com.markit.api.builders.DefaultVisualWatermarkBuilder;
import com.markit.api.WatermarkingMethod;
import com.markit.exceptions.ClosePDFDocumentException;
import com.markit.pdf.WatermarkPdfServiceFactory;
import com.markit.servicelocator.ServiceFactory;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.function.Predicate;

/**
 * @author Oleg Cheban
 * @since 1.3.0
 */
public final class WatermarkPDFBuilder
        extends DefaultVisualWatermarkBuilder<WatermarkPDFService, WatermarkPDFService.WatermarkPDFBuilder>
        implements WatermarkPDFService, WatermarkPDFService.WatermarkPDFBuilder {

    private PDDocument document;

    public WatermarkPDFBuilder(PDDocument pdfDoc, Executor executor) {
        super(watermarks -> getPdfServiceFactory().create(executor).watermark(pdfDoc, watermarks));
        Objects.requireNonNull(pdfDoc, "PDDocument cannot be null");
        this.document = pdfDoc;
    }

    @Override
    public WatermarkPDFBuilder method(WatermarkingMethod watermarkingMethod) {
        getWatermark().setMethod(watermarkingMethod);
        return this;
    }

    @Override
    public WatermarkPDFBuilder dpi(int dpi) {
        getWatermark().setDpi((float) dpi);
        return this;
    }

    @Override
    public WatermarkPDFBuilder documentFilter(Predicate<PDDocument> predicate) {
        getWatermark().setDocumentPredicate(predicate);
        return this;
    }

    @Override
    public WatermarkPDFBuilder pageFilter(Predicate<Integer> predicate) {
        getWatermark().setPagePredicate(predicate);
        return this;
    }

    @NotNull
    @Override
    public byte[] apply() {
        try {
            return super.apply();
        } finally {
            closeDocument();
        }
    }

    private void closeDocument() {
        try {
            document.close();
        } catch (IOException e) {
            throw new ClosePDFDocumentException("Failed to close the document", e);
        }
    }

    private static WatermarkPdfServiceFactory getPdfServiceFactory() {
        return (WatermarkPdfServiceFactory) ServiceFactory.getInstance().getService(WatermarkPdfServiceFactory.class);
    }
}
