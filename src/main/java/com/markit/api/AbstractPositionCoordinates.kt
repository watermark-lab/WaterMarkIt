package com.markit.api

/**
 * @author Oleg Cheban
 * @since 1.0
 */
abstract class AbstractPositionCoordinates : WatermarkPositionCoordinates {
        fun getCoordinatesForPosition(position: WatermarkPosition): WatermarkPositionCoordinates.Coordinates {
        return when (position) {
            WatermarkPosition.CENTER -> center()
            WatermarkPosition.TOP_LEFT -> topLeft()
            WatermarkPosition.TOP_RIGHT -> topRight()
            WatermarkPosition.BOTTOM_LEFT -> bottomLeft()
            WatermarkPosition.BOTTOM_RIGHT -> bottomRight()
        }
    }
}