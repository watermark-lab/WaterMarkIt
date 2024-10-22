package com.markit.api

/**
 *
 * @author Oleg Cheban
 * @since 1.0
 */
interface WatermarkPositionCoordinates {
    fun center(): Coordinates
    fun topLeft(): Coordinates
    fun topRight(): Coordinates
    fun bottomLeft(): Coordinates
    fun bottomRight(): Coordinates
    fun tiled(): List<Coordinates>
    data class Coordinates(val x: Int, val y: Int)
}