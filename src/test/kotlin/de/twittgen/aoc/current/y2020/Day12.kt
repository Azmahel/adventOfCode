package de.twittgen.aoc.current.y2020

import de.twittgen.aoc.current.Day
import de.twittgen.aoc.util.Point2D
import de.twittgen.aoc.current.y2020.Day12.Facing.*
import java.lang.IllegalArgumentException
import kotlin.math.*

class Day12 : Day<List<Instruction>>() {
    override fun String.parse() = lines().map {
        Regex("([A-Z])(\\d+)").matchEntire(it)!!.destructured.let { (operation, amount) ->
            operation to amount.toInt()
        }
    }

    init {
        part1(25, 1294) { it.perform(Ship()) }
        part2(286, 20592) { it.perform(GuidedShip()) }
    }

    private fun List<Instruction>.perform(ship: Ship) = fold(ship) { s, action -> s.perform(action) }
        .position.let { (x, y) -> x.absoluteValue + y.absoluteValue }

    enum class Facing(val direction: Point2D) {
        NORTH(Point2D(-1, 0)), EAST(Point2D(0, 1)), SOUTH(Point2D(1, 0)), WEST(Point2D(0, -1))
    }

    class GuidedShip(
        position: Point2D = Point2D(0,0), facing: Point2D = Point2D(-1,10)
    ): Ship(position, facing) {
        override fun moveInDirection(direction: Point2D, amount: Int) =copy(facing = facing+(direction*amount))
        override fun copy(newPosition: Point2D, facing: Point2D) =  GuidedShip(newPosition, facing)
        override fun moveForward(amount: Int) =  super.moveInDirection(facing, amount)
    }

     open class Ship(
         val position: Point2D = Point2D(0,0), val facing: Point2D = EAST.direction
     ){
         fun perform(action: Pair<String,Int>) = when(action.first) {
             "N" -> moveInDirection(NORTH.direction, action.second)
             "E" -> moveInDirection(EAST.direction, action.second)
             "S" -> moveInDirection(SOUTH.direction, action.second)
             "W" -> moveInDirection(WEST.direction, action.second)
             "L" -> turnClockwise(action.second.toDouble())
             "R" -> turnClockwise((-action.second).toDouble())
             "F" -> moveForward(action.second)
             else -> throw IllegalArgumentException()
         }
         open fun moveInDirection(direction: Point2D, amount: Int) = copy(position+(direction*amount))
         open fun copy(newPosition: Point2D = position, facing: Point2D = this.facing) =  Ship(newPosition, facing)
         open fun moveForward(amount: Int) =  moveInDirection(facing, amount)
         private fun turnClockwise(degrees: Double): Ship {
             val rad = degrees * (PI/180)
             return copy(facing = Point2D(
                 ((cos(rad) * facing.x) - (sin(rad) * facing.y)).roundToInt(),
                 ((sin(rad) * facing.x) + (cos(rad) * facing.y)).roundToInt()
             ))
         }
    }

    override val example = """
        F10
        N3
        F7
        R90
        F11
    """.trimIndent()
}
private typealias Instruction =  Pair<String,Int>
