package com.markit.services.impl;

import com.markit.services.PdfWatermarkDrawService;
import com.markit.services.ImageWatermarker;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.util.Matrix;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Oleg Cheban
 * @since 1.0
 */
public class DefaultPdfWatermarkDrawService implements PdfWatermarkDrawService {
    private final ImageWatermarker imageWatermarker;

    public DefaultPdfWatermarkDrawService(ImageWatermarker imageWatermarker) {
        this.imageWatermarker = imageWatermarker;
    }

    @Override
    public void watermark(PDDocument document, PDFRenderer pdfRenderer, int pageIndex, float dpi, String watermarkText, Color watermarkColor, Boolean trademark) throws IOException {
        var page = document.getPage(pageIndex);
        var image = pdfRenderer.renderImageWithDPI(pageIndex, dpi);
        var baos = new ByteArrayOutputStream();
        ImageIO.write(image, FileType.JPEG.name(), baos);
        var watermarkedImageBytes = imageWatermarker.watermark(baos.toByteArray(), FileType.JPEG, watermarkText, watermarkColor, trademark);
        var pdImage = PDImageXObject.createFromByteArray(document, watermarkedImageBytes, "watermarked");
        replaceImageInPDF(
                document,
                pdImage,
                page,
                page.getCropBox().getLowerLeftX(), page.getCropBox().getLowerLeftY(),
                page.getCropBox().getWidth(), page.getCropBox().getHeight()
        );
    }

    private void replaceImageInPDF(PDDocument document, PDImageXObject watermarkedImage, PDPage page, float x, float y, float width, float height) throws IOException {
        try (var contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.OVERWRITE, false)) {
            int rotation = page.getRotation();
            float pageWidth = page.getMediaBox().getWidth();
            float pageHeight = page.getMediaBox().getHeight();
            float scaleX = 1;
            float scaleY = 1;

            switch (rotation) {
                case 90:
                    contentStream.transform(Matrix.getRotateInstance(Math.toRadians(90), 0, 0));
                    contentStream.transform(Matrix.getTranslateInstance(0, -pageWidth));
                    scaleX = pageHeight / pageWidth;
                    scaleY = pageWidth / pageHeight;
                    break;
                case 180:
                    contentStream.transform(Matrix.getRotateInstance(Math.toRadians(180), 0, 0));
                    contentStream.transform(Matrix.getTranslateInstance(-pageWidth, -pageHeight));
                    break;
                case 270:
                    contentStream.transform(Matrix.getRotateInstance(Math.toRadians(270), 0, 0));
                    contentStream.transform(Matrix.getTranslateInstance(-pageHeight, 0));
                    scaleX = pageHeight / pageWidth;
                    scaleY = pageWidth / pageHeight;
                    break;
            }
            contentStream.transform(Matrix.getScaleInstance(scaleX, scaleY));
            contentStream.drawImage(watermarkedImage, x, y, width, height);
        }
    }
}
