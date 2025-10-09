package com.markit.pdf.overlay.font;

import com.markit.api.WatermarkAttributes;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import java.io.IOException;
import java.io.InputStream;

public class DefaultFontProvider implements FontProvider {

    @Override
    public PDFont loadFont(PDDocument document, WatermarkAttributes attributes) throws IOException {
        final String fontPath = "font/a3arialrusnormal.ttf";
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        try (InputStream fontStream = classloader.getResourceAsStream(fontPath)) {
            if (fontStream == null) {
                throw new IOException("Cyrillic font not found at path: " + fontPath);
            }
            var font = PDType0Font.load(document, fontStream);
            attributes.setCyrillicFont(font);
            return font;
        }
    }

    @Override
    public boolean canHandle(WatermarkAttributes attributes) {
        return attributes.isCyrillic();
    }

    @Override
    public int getPriority() {
        return DEFAULT_PRIORITY;
    }
}
