package de.twittgen.aoc.current.y2021

import de.twittgen.aoc.util.FileUtil
import de.twittgen.aoc.current.y2021.Day22.Command.OFF
import de.twittgen.aoc.current.y2021.Day22.Command.ON
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day22 {
    val input by lazy { FileUtil.readInput("2021/day22").parse() }
    val example = """on x=-20..26,y=-36..17,z=-47..7
on x=-20..33,y=-21..23,z=-26..28
on x=-22..28,y=-29..23,z=-38..16
on x=-46..7,y=-6..46,z=-50..-1
on x=-49..1,y=-3..46,z=-24..28
on x=2..47,y=-22..22,z=-23..27
on x=-27..23,y=-28..26,z=-21..29
on x=-39..5,y=-6..47,z=-3..44
on x=-30..21,y=-8..43,z=-13..34
on x=-22..26,y=-27..20,z=-29..19
off x=-48..-32,y=26..41,z=-47..-37
on x=-12..35,y=6..50,z=-50..-2
off x=-48..-32,y=-32..-16,z=-15..-5
on x=-18..26,y=-33..15,z=-7..46
off x=-40..-22,y=-38..-28,z=23..41
on x=-16..35,y=-41..10,z=-47..6
off x=-32..-23,y=11..30,z=-14..3
on x=-49..-5,y=-3..45,z=-29..18
off x=18..30,y=-20..-8,z=-3..13
on x=-41..9,y=-7..43,z=-33..15
on x=-54112..-39298,y=-85059..-49293,z=-27449..7877
on x=967..23432,y=45373..81175,z=27513..53682""".parse()

    enum class Command { ON, OFF; }
    data class Instruction(val command: Command, val xRange: IntRange, val yRange: IntRange, val zRange: IntRange)
    data class Point3D(val x: Int, val y: Int, val z: Int)
    private fun String.parse() = lines().map { line ->
        val (a,b) = line.split(" ")
        val command = if(a =="on") ON else OFF
        val (x,y,z) = b.split(",").map {
            val (min, max) = it.drop(2).split("..").map { it.toInt() }
            (min..max)
        }
        Instruction(command,x,y,z)
    }

    fun List<Instruction>.run(): Int {
        val map = mutableMapOf<Point3D,Boolean>()
        val targetArea = (-50..50)
        forEach { instruction ->
            with(instruction) {
                xRange.filter { it in targetArea }.forEach { x->
                    yRange.filter { it in targetArea }.forEach { y->
                        zRange.filter { it in targetArea }.forEach { z ->
                            map[Point3D(x, y, z)] = when(command) {
                                OFF -> false
                                ON -> true
                            }
                        }
                    }
                }
            }
        }

        return map.filterKeys { it.x in targetArea && it.y in targetArea && it.z in targetArea }.values.count { it }
    }

    @Test
    fun example() {
        val result = example.run()
        assertEquals(590784, result)
    }

    @Test
    fun example2() {
        val result = example
    }

    @Test
    fun part1() {
        val result = input.run()
        println(result)
    }

    @Test
    fun part2() {
        val result = input
        println(result)
    }
}

