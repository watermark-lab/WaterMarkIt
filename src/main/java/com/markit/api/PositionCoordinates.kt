package com.markit.api

/**
 * @author Oleg Cheban
 * @since 1.0
 */
abstract class PositionCoordinates : WatermarkPositionCoordinates {
    fun getCoordinatesForAttributes(attr: WatermarkAttributes): List<WatermarkPositionCoordinates.Coordinates> {
        var coordinates = when (attr.position) {
            WatermarkPosition.CENTER -> listOf(center())
            WatermarkPosition.TOP_LEFT -> listOf(topLeft())
            WatermarkPosition.TOP_RIGHT -> listOf(topRight())
            WatermarkPosition.BOTTOM_LEFT -> listOf(bottomLeft())
            WatermarkPosition.BOTTOM_RIGHT -> listOf(bottomRight())
            WatermarkPosition.TILED -> tiled()
        }
        if (attr.adjustment.x != 0 || attr.adjustment.y != 0) {
            coordinates = coordinates.map {
                WatermarkPositionCoordinates.Coordinates(
                    it.x + attr.adjustment.x,
                    it.y + attr.adjustment.y
                )
            }
        }
        return coordinates
    }
}
