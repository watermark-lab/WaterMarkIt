package com.markit.api

import java.awt.Color

data class TextWatermarkAttributes (
    var text: String = "",
    var textSize: Int = 100,
    var color: Color = Color.BLACK,
    var opacity: Float = 0.4f,
    var dpi: Float = 300f,
    var trademark: Boolean = false,
    var rotation: Int = 0,
    var method: WatermarkMethod = WatermarkMethod.DRAW,
    var position: WatermarkPosition = WatermarkPosition.CENTER
) {}

