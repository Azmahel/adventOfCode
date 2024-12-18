package de.twittgen.aoc.util

import kotlin.math.absoluteValue
import kotlin.math.sign

data class Point2D (val x : Int, val y: Int) {
    companion object{
        val ORIGIN = Point2D(0,0)
        val directions = ORIGIN.adjacent()
        fun of(p: Pair<Int, Int>) = Point2D(p.first , p.second)
    }
    fun manhattanDistance(other: Point2D = ORIGIN) = (x-other.x).absoluteValue + (y-other.y).absoluteValue
    fun right(c: Int = 1) = Point2D(x + c, y)
    fun left(c: Int = 1) = Point2D(x - c, y)
    fun up(c: Int = 1) = Point2D(x, y + c)
    fun down(c: Int = 1) = Point2D(x, y - c)
    fun east(c: Int = 1) =right(c)
    fun west(c: Int = 1) = left(c)
    fun north(c: Int = 1) = up(c)
    fun south(c: Int = 1) = down(c)
    fun orthogonallyAdjacent() = listOf(up(), down(), left(), right())
    private fun diagonallyAdjacent() = listOf(Point2D(x+1, y+1), Point2D(x+1, y-1), Point2D(x-1, y+1), Point2D(x-1, y-1) )
    fun adjacent() = orthogonallyAdjacent() + diagonallyAdjacent()
    operator fun plus(other: Point2D) = Point2D(x + other.x, y + other.y)
    operator fun minus(other: Point2D) = Point2D(x - other.x, y - other.y)
    operator fun times(i : Int) = Point2D(x*i, y*i)
    operator fun rem(other: Point2D) = Point2D(x % other.x, y % other.y)
    infix fun modWrap(other: Point2D) = (this+other) % other

    fun norm() = Point2D(x.sign, y.sign)
    fun transpose() = Point2D(y,x)
    enum class Direction(val next: (Point2D)-> Point2D, val s: String) {
        UP(Point2D::up, "U"), DOWN(Point2D::down, "D"), LEFT(Point2D::left, "L"), RIGHT(Point2D::right, "R");
        fun turnRight() = when(this) { UP -> RIGHT; RIGHT -> DOWN; DOWN -> LEFT; LEFT -> UP }
    }
}
