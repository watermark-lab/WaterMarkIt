package com.markit.api

import java.awt.Color

data class WatermarkAttributes (
    var text: String = "",
    var textSize: Int = 30,
    var color: Color = Color.BLACK,
    var dpi: Float = 150f,
    var trademark: Boolean = false,
    var method: WatermarkMethod = WatermarkMethod.DRAW,
    var position: WatermarkPosition = WatermarkPosition.CENTER
) {}

