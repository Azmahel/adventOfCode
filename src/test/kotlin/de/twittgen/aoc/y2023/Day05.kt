package de.twittgen.aoc.y2023

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.emptyLine
import de.twittgen.aoc.util.second

class Day05 : Day<Pair<Seeds, List<Mapping>>>() {
    override fun String.parse(): Pair<Seeds, List<Mapping>> {
        val parts = split(emptyLine)
        val seeds = parts.first().split(":").second().trim().split(" ").map(String::toLong)
        val mappings = parts.drop(1).map { part ->
            part.lines().drop(1).map {line ->
                line.trim().split(" ").map(String::toLong).let { (d, s, r) -> s until s+r  to d-s }
            }
        }
        return seeds to mappings
    }
    init {
        part1(35, 165788812) {(seeds, mappings) ->
            mappings.fold(seeds) { current, mapping -> current.map { mapping.map(it) } }.minOf { it }
        }
        part2(46, 1928058) {(seeds, mappings) ->
            mappings.fold(seeds.toPart2()) { current, mapping ->
                current.flatMap { range -> range.mapIntersecting(mapping)} }.minOf { it.first }
        }
    }

    private fun Seeds.toPart2() = chunked(2).map { (a,b) -> a until a+b }

    private fun LongRange.mapIntersecting(mapping: List<OffsetRange>): List<LongRange> {
        val result = mutableListOf<LongRange>()
        var pivot = first
        val remaining = ArrayDeque(mapping.sortedBy { (it, _) ->  it.first }.dropWhile { (it, _) ->  it.last < first })
        while (pivot < last && !remaining.isEmpty()) {
            val (range, offset) = remaining.removeFirst()
            if (pivot < range.first) result.add(pivot until range.first)
            result.add(maxOf(pivot, range.first) + offset .. minOf(range.last, last) + offset)
            pivot = minOf(range.last, last) + 1
        }
        return result.ifEmpty { listOf(this) }
    }

    private fun Mapping.map(seed: Long) =
        firstOrNull { (it,_) -> seed in it }?.let { (_ , offset) -> seed + offset } ?: seed

    override val example = """
        seeds: 79 14 55 13

        seed-to-soil map:
        50 98 2
        52 50 48

        soil-to-fertilizer map:
        0 15 37
        37 52 2
        39 0 15

        fertilizer-to-water map:
        49 53 8
        0 11 42
        42 0 7
        57 7 4

        water-to-light map:
        88 18 7
        18 25 70

        light-to-temperature map:
        45 77 23
        81 45 19
        68 64 13

        temperature-to-humidity map:
        0 69 1
        1 0 69

        humidity-to-location map:
        60 56 37
        56 93 4
    """.trimIndent()

}

typealias Seeds = List<Long>
typealias Mapping = List<OffsetRange>
typealias OffsetRange = Pair<LongRange, Long>