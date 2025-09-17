package com.markit.api.formats.pdf;

import com.markit.api.builders.DefaultWatermarkBuilder;
import com.markit.api.WatermarkingMethod;
import com.markit.pdf.DefaultWatermarkPdfService;
import org.apache.pdfbox.pdmodel.PDDocument;
import com.markit.api.formats.pdf.WatermarkPDFService.*;

import java.util.concurrent.Executor;
import java.util.function.Predicate;

/**
 * @author Oleg Cheban
 * @since 1.3.0
 */
public final class DefaultWatermarkPDFBuilder
        extends DefaultWatermarkBuilder<WatermarkPDFService, WatermarkPDFBuilder>
        implements WatermarkPDFService, WatermarkPDFBuilder {

    public DefaultWatermarkPDFBuilder(PDDocument pdfDoc, Executor executor) {
        super(watermarks -> new DefaultWatermarkPdfService(executor).watermark(pdfDoc, watermarks));
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
}
