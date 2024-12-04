package com.markit.pdf.draw;

import com.markit.api.WatermarkAttributes;
import com.markit.image.ImageWatermarker;
import com.markit.api.FileType;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.util.Matrix;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

/**
 * @author Oleg Cheban
 * @since 1.0
 */
public class DefaultPdfDrawWatermarker implements PdfWatermarker {
    private final ImageWatermarker imageWatermarker;

    public DefaultPdfDrawWatermarker(ImageWatermarker imageWatermarker) {
        this.imageWatermarker = imageWatermarker;
    }

    @Override
    public void watermark(PDDocument document, PDFRenderer pdfRenderer, int pageIndex, List<WatermarkAttributes> attrs) throws IOException {
        var page = document.getPage(pageIndex);
        var image = pdfRenderer.renderImageWithDPI(pageIndex, attrs.stream().map(WatermarkAttributes::getDpi).max(Comparator.naturalOrder()).get());
        var baos = new ByteArrayOutputStream();
        ImageIO.write(image, FileType.JPEG.name(), baos);
        var watermarkedImageBytes = imageWatermarker.watermark(baos.toByteArray(), FileType.JPEG, attrs);
        var pdImage = PDImageXObject.createFromByteArray(document, watermarkedImageBytes, "watermarked");
        replaceImageInPDF(
                document,
                pdImage,
                page,
                page.getCropBox().getLowerLeftX(), page.getCropBox().getLowerLeftY(),
                page.getCropBox().getWidth(), page.getCropBox().getHeight()
        );
    }

    private void replaceImageInPDF(
            PDDocument document,
            PDImageXObject watermarkedImage,
            PDPage page,
            float x,
            float y,
            float width,
            float height) throws IOException {
        try (var contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.OVERWRITE, false)) {
            adjustPageRotation(contentStream, page);
            contentStream.drawImage(watermarkedImage, x, y, width, height);
        }
    }

    /**
     * Pages may have arbitrary rotations (e.g., scanned documents or mixed orientation files).
     * This method provides a solution for handling pages where the rotation is not 0°.
     * It ensures that the transformation matrix is adjusted so that the added watermark appears correctly on the rotated page.
     */
    private void adjustPageRotation(PDPageContentStream contentStream, PDPage page) throws IOException {
        final int rotation = page.getRotation();
        final float pageWidth = page.getMediaBox().getWidth();
        final float pageHeight = page.getMediaBox().getHeight();
        final int D_90 = 90;
        final int D_180 = 180;
        final int D_270 = 270;
        float scaleX = 1;
        float scaleY = 1;

        switch (rotation) {
            case D_90:
                contentStream.transform(Matrix.getRotateInstance(Math.toRadians(D_90), 0, 0));
                contentStream.transform(Matrix.getTranslateInstance(0, -pageWidth));
                scaleX = pageHeight / pageWidth;
                scaleY = pageWidth / pageHeight;
                break;
            case D_180:
                contentStream.transform(Matrix.getRotateInstance(Math.toRadians(D_180), 0, 0));
                contentStream.transform(Matrix.getTranslateInstance(-pageWidth, -pageHeight));
                break;
            case D_270:
                contentStream.transform(Matrix.getRotateInstance(Math.toRadians(D_270), 0, 0));
                contentStream.transform(Matrix.getTranslateInstance(-pageHeight, 0));
                scaleX = pageHeight / pageWidth;
                scaleY = pageWidth / pageHeight;
                break;
        }

        contentStream.transform(Matrix.getScaleInstance(scaleX, scaleY));
    }
}
