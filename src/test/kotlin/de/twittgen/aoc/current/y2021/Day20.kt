package de.twittgen.aoc.current.y2021

import de.twittgen.aoc.util.FileUtil
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day20 {

    val input by lazy { FileUtil.readInput("2021/day20").parse() }
    val example = """..#.#..#####.#.#.#.###.##.....###.##.#..###.####..#####..#....#..#..##..###..######.###...####..#..#####..##..#.#####...##.#.#..#.##..#.#......#.###.######.###.####...#.##.##..#..#..#####.....#.#....###..#.##......#.....#..#..#..##..#...##.######.####.####.#.#...#.......#..#.#.#...####.##.#......#..#...##.#.##..#...##.#.##..###.#......#.#.......#.#.#.####.###.##...#.....####.#..#..#.##.#....##..#.####....##...##..#...#......#.#.......#.......##..####..#...#.#.#...##..#.#..###..#####........#..####......#..#

#..#.
#....
##..#
..#..
..###""".parse()

    private fun String.parse(): Pair<List<Int>, Image> {
        val algorithm = lines().first().map { if(it == '#') 1 else 0 }
        val image = lines().drop(2)
        val map = image.flatMapIndexed { x, line ->
            line.mapIndexedNotNull { y, c ->
                if(c == '#') ((x to y) to  1) else((x to y) to  0)
            }
        }.toMap().withDefault { 0 }
        return algorithm to map
    }
    private fun Image.print() = (keys.minByOrNull {it.first }!!.first..keys.maxByOrNull {it.first }!!.first).map { y ->
            (keys.minByOrNull {it.second }!!.second..keys.maxByOrNull {it.second }!!.second).map { x ->
                when(get(x to y)) {
                    1 -> "#"
                    0 -> "."
                    else -> "o"
                }
            }.joinToString("")
        }.joinToString("\n")

    private fun Image.enhance(algorithm : List<Int>, default : Int = 0): Image {
        val points = keys.flatMap { (x,y) ->
            (x-1..x+1).flatMap { nx ->
                (y-1..y+1).map { ny ->
                    (nx to ny)
                }
            }
        }
        return points.associate { (x, y) ->
            val score = (x - 1..x + 1).flatMap { nx ->
                (y - 1..y + 1).map { ny ->
                    getOrDefault((nx to ny), default)
                }
            }.joinToString("")
            (x to y) to algorithm[score.toInt(2)]
        }//.filterValues { it == 1 }
    }

    @Test
    fun example() {
        val result = with(example) {second.enhance(first).enhance(first).values.sum() }
        assertEquals(35, result)
    }

    @Test
    fun example2() {
        val result = (1..50).fold(example.second) { it, _ -> it.enhance(example.first) }.values.sum()
        assertEquals(3351, result)
    }

    @Test
    fun part1() {
        val result = with(input) { second.enhance(first).enhance(first,1).values.sum() }
        println(result)
    }

    @Test
    fun part2() {
        val result = (0..49).fold(input.second) { it, i -> it.enhance(input.first, i%2) }.values.sum()
        println(result)
    }
}

 private typealias Image = Map<Pair<Int,Int>, Int>

