package com.markit.api.positioning

import com.markit.api.WatermarkAttributes

/**
 * @author Oleg Cheban
 * @since 1.0
 */
abstract class PositionCoordinates(
    protected val pageWidth: Int,
    protected val pageHeight: Int,
    protected val watermarkWidth: Int,
    protected val watermarkHeight: Int
) : WatermarkPositionCoordinates {

    fun getCoordinatesForAttributes(attr: WatermarkAttributes): List<WatermarkPositionCoordinates.Coordinates> {
        // If custom coordinates are being used, return them directly
        if (attr.customCoordinates) {
            return listOf(WatermarkPositionCoordinates.Coordinates(attr.positionCoordinates.x, attr.positionCoordinates.y))
        }
        
        var coordinates = when (attr.position) {
            WatermarkPosition.CENTER -> listOf(center())
            WatermarkPosition.TOP_LEFT -> listOf(topLeft())
            WatermarkPosition.TOP_RIGHT -> listOf(topRight())
            WatermarkPosition.TOP_CENTER -> listOf(topCenter())
            WatermarkPosition.BOTTOM_LEFT -> listOf(bottomLeft())
            WatermarkPosition.BOTTOM_RIGHT -> listOf(bottomRight())
            WatermarkPosition.BOTTOM_CENTER -> listOf(bottomCenter())
            WatermarkPosition.TILED -> tiled(attr)
        }
        
        // Apply position adjustment if needed
        if (attr.positionCoordinates.x != 0 || attr.positionCoordinates.y != 0) {
            coordinates = coordinates.map {
                WatermarkPositionCoordinates.Coordinates(
                    it.x + attr.positionCoordinates.x,
                    it.y + attr.positionCoordinates.y
                )
            }
        }
        return coordinates
    }

    override fun tiled(attr: WatermarkAttributes): List<WatermarkPositionCoordinates.Coordinates> {
        val numHorizontal = (pageWidth + watermarkWidth - 1) / watermarkWidth
        val numVertical = (pageHeight + watermarkHeight - 1) / watermarkHeight
        return (0 until numHorizontal).flatMap { i ->
            (0 until numVertical).map { j ->
                WatermarkPositionCoordinates.Coordinates(
                    (i * watermarkWidth) + (i * attr.horizontalSpacing),
                    (j * watermarkHeight) + (j * attr.verticalSpacing)
                )
            }
        }
    }
}
