package com.markit.pdf.overlay;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Oleg Cheban
 * @since 1.3.5
 */
public class CyrillicFontLoader {
    public static PDType0Font loadDefault(PDDocument document) throws IOException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        final String path = "font/a3arialrusnormal.ttf";
        InputStream arialFont = classloader.getResourceAsStream(path);
        return PDType0Font.load(document, arialFont);
    }
}