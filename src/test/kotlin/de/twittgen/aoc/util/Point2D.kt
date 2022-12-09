package de.twittgen.aoc.util

import kotlin.math.absoluteValue

data class Point2D (val x : Int, val y: Int) {
    companion object{
        val ORIGIN = Point2D(0,0)
    }
    fun manhattanDistanceTo(other: Point2D) = (x-other.x).absoluteValue + (y-other.y).absoluteValue
    fun right() = Point2D(x + 1, y) 
    fun left() = Point2D(x - 1, y)
    fun up() = Point2D(x, y + 1)
    fun down() = Point2D(x, y - 1)
    fun orthogonallyAdjacent() = listOf(up(), down(), left(), right())
    fun diagonallyAdjacent() = listOf(Point2D(x+1, y+1), Point2D(x+1, y-1), Point2D(x-1, y+1), Point2D(x-1, y-1) )
    fun adjacent() = orthogonallyAdjacent() + diagonallyAdjacent()
}