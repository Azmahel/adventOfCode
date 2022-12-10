package de.twittgen.aoc.current.y2021

import de.twittgen.aoc.Day
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


class Day20 : Day<Pair<Algorithm, Image>>() {
    override fun String.parse(): Pair<Algorithm, Image> {
        val algorithm = lines().first().map { if(it == '#') 1 else 0 }
        val map = lines().drop(2).flatMapIndexed { x, line -> line.mapIndexedNotNull { y, c ->
                if (c == '#') ((x to y) to 1) else ((x to y) to 0)
        } }.toMap().withDefault { 0 }
        return algorithm to map
    }

    init {
        part1(35, 5708) { (alg, image) -> image.enhance(alg).enhance(alg).values.sum() }
        part2(3351, 20895) { (alg, image) -> (1..50).fold(image) { i, _ -> i.enhance(alg) }.values.sum() }
    }

    private fun Image.enhance(algorithm : Algorithm, default : Int = 0): Image {
        val points = keys.flatMap { (x,y) -> (x-1..x+1).flatMap { nx -> (y-1..y+1).map { ny ->
            (nx to ny)
        } } }
        return points.associate { (x, y) ->
            val score = (x - 1..x + 1).flatMap { nx -> (y - 1..y + 1).map { ny ->
                getOrDefault((nx to ny), default)
            } }.joinToString("")
            (x to y) to algorithm[score.toInt(2)]
        }
    }

    override val example = """
        ..#.#..#####.#.#.#.###.##.....###.##.#..###.####..#####..#....#..#..##..###..######.###...####..#..#####..##..#.#####...##.#.#..#.##..#.#......#.###.######.###.####...#.##.##..#..#..#####.....#.#....###..#.##......#.....#..#..#..##..#...##.######.####.####.#.#...#.......#..#.#.#...####.##.#......#..#...##.#.##..#...##.#.##..###.#......#.#.......#.#.#.####.###.##...#.....####.#..#..#.##.#....##..#.####....##...##..#...#......#.#.......#.......##..####..#...#.#.#...##..#.#..###..#####........#..####......#..#
        
        #..#.
        #....
        ##..#
        ..#..
        ..###
    """.trimIndent()
}

private typealias Image = Map<Pair<Int,Int>, Int>
private typealias Algorithm = List<Int>

