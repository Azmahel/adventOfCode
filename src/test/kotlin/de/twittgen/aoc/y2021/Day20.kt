package de.twittgen.aoc.y2021

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.Point2D


class Day20 : Day<Pair<Algorithm, Image>>() {
    override fun String.parse(): Pair<Algorithm, Image> {
        val algorithm = lines().first().map { if(it == '#') 1 else 0 }.convert()
        val image = lines().drop(2).flatMapIndexed { x, line -> line.mapIndexedNotNull { y, c ->
            if (c == '#') Point2D(x, y) else null
        } }.toSet()
        return algorithm to image
    }

    init {
        part1(35, 5708) { (alg, image) -> image.enhance(alg).enhance(alg).size }
        part2(3351, 20895) { (alg, image) -> (1..50).fold(image) { i, _ -> i.enhance(alg) }.size }
    }

    private fun Image.enhance(algorithm : Algorithm): Image {
        val (minX, maxX, minY, maxY) = listOf(minOf{ it.x }, maxOf {it.x }, minOf{ it.y }, maxOf {it.y })
        return (minX-1..maxX+1).flatMap { x -> (minY-1..maxY+1).mapNotNull { y -> Point2D(x,y).let { pixel ->
            val scan = (pixel.adjacent() + pixel).filter { it in this }.map { it - pixel }.toSet()
            if(scan in algorithm) pixel else null
        }}}.toSet()
    }

    private fun List<Int>.convert() = withIndex().filterNot{ it.value ==0 }.map { (i, _) ->
        i.toString(2).padStart(9, '0') .chunked(3).flatMapIndexed { x, l -> l.mapIndexedNotNull { y, i ->
            if (i == '1') Point2D(x-1, y-1) else null
        } }.toSet()
    }.toSet()

    override val example = """
        ..#.#..#####.#.#.#.###.##.....###.##.#..###.####..#####..#....#..#..##..###..######.###...####..#..#####..##..#.#####...##.#.#..#.##..#.#......#.###.######.###.####...#.##.##..#..#..#####.....#.#....###..#.##......#.....#..#..#..##..#...##.######.####.####.#.#...#.......#..#.#.#...####.##.#......#..#...##.#.##..#...##.#.##..###.#......#.#.......#.#.#.####.###.##...#.....####.#..#..#.##.#....##..#.####....##...##..#...#......#.#.......#.......##..####..#...#.#.#...##..#.#..###..#####........#..####......#..#
        
        #..#.
        #....
        ##..#
        ..#..
        ..###
    """.trimIndent()
}

private typealias Image = Set<Point2D>
private typealias Algorithm = Set<Set<Point2D>>

