package com.markit.api

/**
 * @author Oleg Cheban
 * @since 1.0
 */
abstract class PositionCoordinates : WatermarkPositionCoordinates {
        fun getCoordinatesForPosition(position: WatermarkPosition): List<WatermarkPositionCoordinates.Coordinates> {
        return when (position) {
            WatermarkPosition.CENTER -> listOf(center())
            WatermarkPosition.TOP_LEFT -> listOf(topLeft())
            WatermarkPosition.TOP_RIGHT -> listOf(topRight())
            WatermarkPosition.BOTTOM_LEFT -> listOf(bottomLeft())
            WatermarkPosition.BOTTOM_RIGHT -> listOf(bottomRight())
            WatermarkPosition.TILED -> tiled()
        }
    }
}