package de.twittgen.aoc.util

import kotlin.math.absoluteValue
import kotlin.math.sign

data class Point2D (val x : Int, val y: Int) {
    companion object{
        val ORIGIN = Point2D(0,0)
        fun of(p: Pair<Int, Int>) = Point2D(p.first , p.second)
    }
    fun manhattanDistanceTo(other: Point2D) = (x-other.x).absoluteValue + (y-other.y).absoluteValue
    fun right() = Point2D(x + 1, y) 
    fun left() = Point2D(x - 1, y)
    fun up() = Point2D(x, y + 1)
    fun down() = Point2D(x, y - 1)
    fun orthogonallyAdjacent() = listOf(up(), down(), left(), right())
    private fun diagonallyAdjacent() = listOf(Point2D(x+1, y+1), Point2D(x+1, y-1), Point2D(x-1, y+1), Point2D(x-1, y-1) )
    fun adjacent() = orthogonallyAdjacent() + diagonallyAdjacent()
    operator fun plus(other: Point2D) = Point2D(x + other.x, y + other.y)
    operator fun minus(other: Point2D) = Point2D(x - other.x, y - other.y)
    operator fun times(i : Int) = Point2D(x*i, y*i)
    fun norm() = Point2D(x.sign, y.sign)
    fun transpose() = Point2D(y,x)
    enum class Direction(val next: (Point2D)-> Point2D){
        UP(Point2D::up), DOWN(Point2D::down), LEFT(Point2D::left), RIGHT(Point2D::right)
    }
}