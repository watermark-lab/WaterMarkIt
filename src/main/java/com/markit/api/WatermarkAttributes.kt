package com.markit.api

import com.markit.api.positioning.WatermarkPosition
import com.markit.api.positioning.WatermarkPositionCoordinates
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.font.PDFont
import java.awt.Color
import java.awt.image.BufferedImage
import java.util.*
import java.util.function.Predicate

/**
 * @author Oleg Cheban
 * @since 1.0
 */
data class WatermarkAttributes (
    var text: String = "",
    var image: Optional<BufferedImage> = Optional.empty(),
    var size: Int = 50,
    var color: Color = Color.BLACK,
    var opacity: Int = 40,
    var font: Font = Font.ARIAL,
    var dpi: Float = 300f,
    var trademark: Boolean = false,
    var rotationDegrees: Int = 0,
    var method: WatermarkingMethod = WatermarkingMethod.DRAW,
    var position: WatermarkPosition = WatermarkPosition.CENTER,
    var positionCoordinates: WatermarkPositionCoordinates.Coordinates = WatermarkPositionCoordinates.Coordinates(0, 0),
    var customCoordinates: Boolean = false,
    var verticalSpacing: Int = 50,
    var horizontalSpacing: Int = 50,
    var documentPredicate: Predicate<PDDocument> = Predicate { true },
    var pagePredicate: Predicate<Int> = Predicate { true },
    var visible: Boolean = true,
    var isBold: Boolean = false,
    var adjustTextSizeCf: Float = 2.5f,
    var cyrillicFont: PDFont? = null
) {
    //calculated attributes
    val isCyrillic: Boolean
        get() = text.any { Character.UnicodeBlock.of(it) == Character.UnicodeBlock.CYRILLIC }

    val resolvedPdfFont
        get() = when {
            isCyrillic -> requireNotNull(cyrillicFont) { "Cyrillic font must be provided for Cyrillic text" }
            isBold -> font.boldPdFont
            else -> font.pdFont
        }

    val pdfWatermarkTextWidth: Float
        get() = resolvedPdfFont.getStringWidth(text) / 1000f * size / adjustTextSizeCf

    val pdfWatermarkTextHeight: Float
        get() = resolvedPdfFont.fontDescriptor.capHeight / 1000f * size / adjustTextSizeCf

    val pdfTextSize: Float
        get() = size / adjustTextSizeCf

    val imageTextSize: Float
        get() = size * 1.7f
}