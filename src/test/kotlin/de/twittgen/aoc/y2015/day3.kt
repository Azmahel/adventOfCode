package de.twittgen.aoc.y2015

import de.twittgen.aoc.y2019.shared.util.FileUtil
import org.junit.jupiter.api.Test

class day3 {
    val input = FileUtil.readInput("2015/day3")
    val examples = listOf(">", "^>v<", "^v^v^v^v^v")
    val examples2 = listOf("^v", "^>v<", "^v^v^v^v^v")
    @Test
    fun example() {
        val paths =  examples.map {perform(it)}
        val visitedHouses = paths.map { it.distinct().size }
        assert(
            visitedHouses == listOf(2,4,2)
        )
    }

    @Test
    fun part1() {
        val path =  perform(input)
        val visitedHouses =  path.distinct().size
        println(visitedHouses)
    }

    @Test
    fun example2() {
        val paths =  examples2.map {perform(it,2)}
        val visitedHouses = paths.map { it.distinct().size }
        assert(
            visitedHouses == listOf(3,3,11)
        )
    }

    @Test
    fun part2() {
        val path =  perform(input,2)
        val visitedHouses =  path.distinct().size
        println(visitedHouses)
    }

    private fun perform(command: String, operatorCount: Int = 1): List<Pair<Int,Int>> {
        var positions = (0 until operatorCount).map {  0 to 0 }
        command.windowed(operatorCount, operatorCount).forEach { subcommand ->
            val last = positions.takeLast(operatorCount)
            subcommand.mapIndexed { i, c ->
                when (c) {
                    '<' -> positions += last[i].first - 1 to last[i].second
                    '>' -> positions += last[i].first + 1 to last[i].second
                    'v' -> positions += last[i].first to last[i].second + 1
                    '^' -> positions += last[i].first to last[i].second - 1
                }
            }
        }
        return positions
    }
}