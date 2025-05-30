package com.markit.core.formats.pdf;

import com.markit.core.builders.DefaultWatermarkBuilder;
import com.markit.core.WatermarkingMethod;
import com.markit.exceptions.ClosePDFDocumentException;
import com.markit.pdf.DefaultWatermarkPdfService;
import org.apache.pdfbox.pdmodel.PDDocument;
import com.markit.core.formats.pdf.WatermarkPDFService.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.function.Predicate;

/**
 * @author Oleg Cheban
 * @since 1.3.0
 */
public final class DefaultWatermarkPDFBuilder
        extends DefaultWatermarkBuilder<WatermarkPDFService, WatermarkPDFBuilder>
        implements WatermarkPDFService, WatermarkPDFBuilder {

    private final PDDocument document;

    public DefaultWatermarkPDFBuilder(PDDocument pdfDoc, Executor executor) {
        super(watermarks -> new DefaultWatermarkPdfService(executor).watermark(pdfDoc, watermarks));
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
        var res = super.apply();
        closeDocument();
        return res;
    }

    private void closeDocument() {
        try {
            this.document.close();
        } catch (IOException e) {
            throw new ClosePDFDocumentException("Failed to close the document", e);
        }
    }
}
