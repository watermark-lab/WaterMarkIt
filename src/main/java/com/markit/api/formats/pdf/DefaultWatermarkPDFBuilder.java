package com.markit.api.formats.pdf;

import com.markit.api.builders.DefaultWatermarkBuilder;
import com.markit.api.WatermarkingMethod;
import com.markit.pdf.DefaultWatermarkPdfService;
import org.apache.pdfbox.pdmodel.PDDocument;
import com.markit.api.formats.pdf.WatermarkPDFService.*;

import java.lang.ref.Cleaner;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.function.Predicate;

/**
 * @author Oleg Cheban
 * @since 1.3.0
 */
public final class DefaultWatermarkPDFBuilder
        extends DefaultWatermarkBuilder<WatermarkPDFService, WatermarkPDFBuilder>
        implements WatermarkPDFService, WatermarkPDFBuilder {

    private static final Cleaner CLEANER = Cleaner.create();

    public DefaultWatermarkPDFBuilder(PDDocument pdfDoc, Executor executor) {
        super(watermarks -> new DefaultWatermarkPdfService(executor).watermark(pdfDoc, watermarks));
        Objects.requireNonNull(pdfDoc, "pdf file cannot be null");
        CLEANER.register(this, new DocumentState(pdfDoc));
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

    private static final class DocumentState implements Runnable {
        private final PDDocument document;

        private DocumentState(PDDocument document) {
            this.document = document;
        }

        @Override
        public void run() {
            try {
                document.close();
            } catch (Exception ignored) {
                // Best-effort cleanup during GC; exceptions cannot be propagated here.
            }
        }
    }
}
