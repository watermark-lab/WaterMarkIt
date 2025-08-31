package com.markit.api

import org.apache.pdfbox.pdmodel.font.PDFont
import org.apache.pdfbox.pdmodel.font.PDType1Font

enum class Font(val pdFont: PDFont, val awtFontName: String) {
    ARIAL(PDType1Font.HELVETICA, "Arial"),
    TIMES_NEW_ROMAN(PDType1Font.TIMES_ROMAN, "Times New Roman"),
    COURIER(PDType1Font.COURIER, "Courier New")
}