package com.markit.api

import java.awt.Color
import java.awt.image.BufferedImage
import java.util.*

data class WatermarkAttributes (
    var text: String = "",
    var size: Int = 100,
    var color: Color = Color.BLACK,
    var opacity: Float = 0.4f,
    var dpi: Float = 300f,
    var trademark: Boolean = false,
    var rotation: Int = 0,
    var method: WatermarkMethod = WatermarkMethod.DRAW,
    var position: WatermarkPosition = WatermarkPosition.CENTER,
    var image: Optional<BufferedImage> = Optional.empty()
) {}

