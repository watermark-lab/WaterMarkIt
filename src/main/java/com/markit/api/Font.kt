package com.markit.api

import org.apache.pdfbox.pdmodel.font.PDFont
import org.apache.pdfbox.pdmodel.font.PDType1Font

enum class Font(val pdFont: PDFont, val awtFontName: String, val boldPdFont: PDFont) {
    ARIAL(PDType1Font.HELVETICA, "Arial", PDType1Font.HELVETICA_BOLD),
    TIMES_NEW_ROMAN(PDType1Font.TIMES_ROMAN, "Times New Roman", PDType1Font.TIMES_BOLD),
    COURIER(PDType1Font.COURIER, "Courier New", PDType1Font.COURIER_BOLD);
}
