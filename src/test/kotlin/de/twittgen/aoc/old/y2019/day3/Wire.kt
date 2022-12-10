package de.twittgen.aoc.old.y2019.day3

import de.twittgen.aoc.util.Point2D
import java.lang.IllegalArgumentException

private typealias Instruct = Pair<String,Int>
private const val UP = "U"
private const val DOWN = "D"
private const val LEFT = "L"
private const val RIGHT = "R"

class Wire(instructions: List<Instruct>) {
    private val path: List<Point2D> = generatePath(instructions)

    fun getDistanceTo(point: Point2D): Int = this.path.indexOf(point)
    fun getClosestIntersectionByWireLengthWith(other: Wire) = getIntersectionsWith(other).minByOrNull { this.getDistanceTo(it) + other.getDistanceTo(it)}!!
    fun getClosestIntersectionWith(other:Wire, to: Point2D): Point2D = getIntersectionsWith(other).minByOrNull { it.manhattanDistanceTo(to) }!!

    private fun getIntersectionsWith(other: Wire): List<Point2D> = this.path.intersect(other.path).filterNot{ it == Point2D.ORIGIN }
    private fun generatePath(instructions: List<Instruct>): List<Point2D> {
        var currentPosition = Point2D.ORIGIN
        val path = mutableListOf(currentPosition)
        instructions.forEach{ (operation, count) ->
            when (operation) {
                UP -> repeat( count) {
                    currentPosition = Point2D(currentPosition.x,currentPosition.y-1)
                    path.add(currentPosition)
                }
                DOWN -> repeat( count) {
                    currentPosition = Point2D(currentPosition.x,currentPosition.y+1)
                    path.add(currentPosition)
                }
                RIGHT -> repeat(count) {
                    currentPosition = Point2D(currentPosition.x+1, currentPosition.y)
                    path.add(currentPosition)
                }
                LEFT -> repeat(count) {
                    currentPosition = Point2D(currentPosition.x-1, currentPosition.y)
                    path.add(currentPosition)
                }
                else -> throw  IllegalArgumentException("Unknown direction $operation")
            }
        }
        return path
    }
}