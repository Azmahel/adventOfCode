package de.twittgen.aoc.y2024
import de.twittgen.aoc.Day
import de.twittgen.aoc.util.repeat

class Day11: Day<Map<Rock, Long>>() {
    override fun String.parse() =
        split(" ").map { it.toLong() }.groupBy { it }.mapValues { (_, v) -> v.size.toLong() }

    init {
        part1(55312, 233050) { it.smashAllTimes(25).values.sum() }
        part2(65601038650482, 276661131175807) { it.smashAllTimes(75).values.sum() }
    }

    private fun Map<Rock, Long>.smashAllTimes(times: Int = 25) = this.repeat(times) { it.smashAll() }

    private fun Map<Rock, Long>.smashAll(): Map<Rock, Long> = flatMap { (r, n) -> r.smash().map { it to n } }
        .groupBy { it.first }.mapValues { (_, v) -> v.sumOf { it.second } }

    private fun Rock.smash() = when {
        equals(0L) -> listOf(1L)
        toString().length % 2 == 0 ->
            toString().let { listOf(it.take(it.length/2).toLong(), it.drop(it.length/2).toLong()) }
        else -> listOf(times(2024))
    }
    override val example= "125 17"
}
private typealias Rock = Long