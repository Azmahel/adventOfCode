package de.twittgen.aoc.y2020

import de.twittgen.aoc.util.Point2D
import de.twittgen.aoc.util.FileUtil
import de.twittgen.aoc.y2020.day12.Facing.*
import org.junit.jupiter.api.Test
import java.lang.IllegalArgumentException
import kotlin.math.*

class day12 {
    val input = FileUtil.readInput("2020/day12")
    val example = """
        F10
        N3
        F7
        R90
        F11
    """.trimIndent()


    enum class Facing(val direction: Point2D) {
        NORTH(Point2D(-1, 0)),
        EAST(Point2D(0, 1)),
        SOUTH(Point2D(1, 0)),
        WEST(Point2D(0, -1))
    }

    fun parseInput(s:String): List<Pair<String, Int>> {
        return s.lines().map {
            val (operation, amount) = Regex("([A-Z])(\\d+)").matchEntire(it)!!.destructured
            operation to amount.toInt()
        }

    }

    @Test
    fun example() {
        val commands = parseInput(example)
        val final = commands.fold(Ship()) { ship, action ->
            ship.performAction(action)
        }
        assert(
            final.position.x.absoluteValue + final.position.y.absoluteValue == 25
        )
    }

    @Test
    fun part1() {
        val commands = parseInput(input)
        val final = commands.fold(Ship()) { ship, action ->
            ship.performAction(action)
        }
        println(
            final.position.x.absoluteValue + final.position.y.absoluteValue
        )
    }

    @Test
    fun part2() {
        val commands = parseInput(input)
        val final = commands.fold(GuidedShip()) { ship, action ->
            ship.performAction(action) as GuidedShip
        }
        println(
            final.position.x.absoluteValue + final.position.y.absoluteValue
        )
    }



    class GuidedShip(position: Point2D = Point2D(0,0), facing: Point2D = Point2D(-1,10) ) : Ship(position, facing)  {
        override fun copy(position: Point2D, facing: Point2D): GuidedShip {
            return GuidedShip(position, facing)
        }

        override fun moveInDirection(direction: Point2D, amount: Int): GuidedShip {
            val newFacing = Point2D(
                facing.x + (direction.x * amount),
                facing.y + (direction.y * amount)
            )
            return copy(position, newFacing)
        }

        override fun moveForward(amount: Int): GuidedShip {
            return super.moveInDirection(facing, amount) as GuidedShip
        }
    }

     open class Ship(val position: Point2D = Point2D(0,0), val facing: Point2D = EAST.direction ) {
        protected open fun copy(position: Point2D = this.position, facing: Point2D = this.facing): Ship {
            return Ship(position, facing)
        }
         fun performAction(action: Pair<String,Int>): Ship {
           return when(action.first) {
               "N" -> moveInDirection(NORTH.direction, action.second)
               "E" -> moveInDirection(EAST.direction, action.second)
               "S" -> moveInDirection(SOUTH.direction, action.second)
               "W" -> moveInDirection(WEST.direction, action.second)
               "L" -> turnClockwise(action.second.toDouble())
               "R" -> turnClockwise((-action.second).toDouble())
               "F" -> moveForward(action.second)
               else -> throw IllegalArgumentException()
            }
        }

         protected open fun moveForward(amount: Int): Ship {
             return moveInDirection(facing, amount)
         }

         private fun turnClockwise(degrees: Double): Ship {
             val rad = degrees * (PI/180)
            val newFacing = Point2D(
                ((cos(rad) * facing.x) - (sin(rad) * facing.y)).roundToInt(),
                ((sin(rad) * facing.x) + (cos(rad) * facing.y)).roundToInt()
            )
            return copy(position, newFacing)
        }

        protected open fun moveInDirection(direction: Point2D, amount: Int): Ship {
           val newPosition = Point2D(
               position.x + (direction.x * amount),
               position.y + (direction.y * amount)
           )
            return copy(newPosition, facing)
        }
    }


}