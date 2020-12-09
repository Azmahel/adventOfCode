package de.twittgen.aoc.y2019.shared

import kotlin.math.absoluteValue

data class Point2D (val x : Int, val y: Int) {
    companion object{
        val ORIGIN = Point2D(0,0)
    }
   fun manhattanDistanceTo(other: Point2D) = (x-other.x).absoluteValue + (y-other.y).absoluteValue
}