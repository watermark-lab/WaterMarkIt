package com.markit.api

import java.awt.Color

data class WatermarkAttributes (
    var text: String = "",
    var textSize: Int = 100,
    var color: Color = Color.BLACK,
    var dpi: Float = 300f,
    var trademark: Boolean = false,
    var method: WatermarkMethod = WatermarkMethod.DRAW,
    var position: WatermarkPosition = WatermarkPosition.CENTER
) {}

