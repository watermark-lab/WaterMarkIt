package com.markit.video.ffmpeg.text

import com.markit.api.positioning.WatermarkPosition

object WatermarkPositionHelper {

    @JvmStatic
    fun calculateWatermarkPosition(
        position: WatermarkPosition,
        useCustomCoordinates: Boolean = false,
        customX: String? = null,
        customY: String? = null
    ): Coordinates {
        // If custom coordinates are specified, use them directly
        if (useCustomCoordinates) {
            return Coordinates(customX ?: "0", customY ?: "0")
        }

        return when (position) {
            WatermarkPosition.TOP_LEFT -> Coordinates("10", "10")  // 10px padding
            WatermarkPosition.TOP_CENTER -> Coordinates("(w-text_w)/2", "10")
            WatermarkPosition.TOP_RIGHT -> Coordinates("w-tw-10", "10")  // 10px padding
            WatermarkPosition.CENTER -> Coordinates("(w-text_w)/2", "(h-text_h)/2")
            WatermarkPosition.BOTTOM_LEFT -> Coordinates("10", "h-th-10")
            WatermarkPosition.BOTTOM_CENTER -> Coordinates("(w-text_w)/2", "h-th-10")
            WatermarkPosition.BOTTOM_RIGHT -> Coordinates("w-tw-10", "h-th-10")
            WatermarkPosition.TILED -> Coordinates("(w-text_w)/2", "(h-text_h)/2") // todo tiled logic
        }
    }
}