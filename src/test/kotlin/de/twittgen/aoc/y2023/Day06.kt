package de.twittgen.aoc.y2023

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.pq
import de.twittgen.aoc.util.product
import de.twittgen.aoc.util.second
import kotlin.math.*

class Day06 : Day<List<Pair<String, String>>>() {
    override fun String.parse() = lines().map {
        numbers.findAll(it).flatMap { it.destructured.toList() }.toList()
    }.let { it.first().zip(it.second()) }

    init {
        part1(288, 227850) { it.toPart1().map { race -> race.getPqBeating() }.product() }
        part2(71503, 42948149) { it.toPart2().getPqBeating() }
    }

    private val numbers = Regex("(\\d+)")
    private fun List<Pair<String, String>>.toPart2() =
        fold("" to "") { a, b -> a.first + b.first to a.second + b.second }.let { (a,b) -> a.toLong() to b.toLong() }
    private fun List<Pair<String, String>>.toPart1() = map {it.first.toLong() to it.second.toLong()}

    // this brute force approach takes ~100ms for part2, and we cant have that
    private fun Pair<Long,Long>.getBeating() = (1 until first).fold(0L) { p, it ->  if(((first - it) * it) > second) p+1 else p }

    private fun Pair<Long, Long>.getPqBeating()= pq(this.first.toDouble(), this.second.toDouble()).let { (x1,x2) ->
        x2.toLong() - 1 - (x1.toLong() - 1) - if (x1.roundToLong() - x1 == 0.0) 1 else 0
    }

    override val example = """
        Time:      7  15   30
        Distance:  9  40  200
    """.trimIndent()

}