package com.markit.api

import com.markit.api.positioning.WatermarkPosition
import com.markit.api.positioning.WatermarkPositionCoordinates
import org.apache.pdfbox.pdmodel.PDDocument
import java.awt.Color
import java.awt.image.BufferedImage
import java.util.*
import java.util.function.Predicate

data class WatermarkAttributes (
    var text: Optional<String> = Optional.empty(),
    var size: Int = 100,
    var color: Color = Color.BLACK,
    var opacity: Float = 0.4f,
    var dpi: Optional<Float> = Optional.empty(),
    var trademark: Boolean = false,
    var rotationDegrees: Int = 0,
    var method: WatermarkingMethod = WatermarkingMethod.DRAW,
    var position: WatermarkPosition = WatermarkPosition.CENTER,
    var positionAdjustment: WatermarkPositionCoordinates.Coordinates = WatermarkPositionCoordinates.Coordinates(0, 0),
    var verticalSpacing: Int = 50,
    var horizontalSpacing: Int = 50,
    var image: Optional<BufferedImage> = Optional.empty(),
    var documentPredicate: Predicate<PDDocument> = Predicate { true },
    var pagePredicate: Predicate<Int> = Predicate { true },
    var watermarkEnabled: Boolean = true
)