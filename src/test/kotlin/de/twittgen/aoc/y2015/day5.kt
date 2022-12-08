package de.twittgen.aoc.y2015

import de.twittgen.aoc.util.FileUtil
import org.junit.jupiter.api.Test

class day5 {
    val input = FileUtil.readInput("2015/day5")
    val example = """
        ugknbfddgicrmopn
        aaa
        jchzalrnumimnmhp
        haegwjzuvuyypxyu
        dvszwmarrgswjxmb
    """.trimIndent()

    @Test
    fun example() {
        val names = example.lines()
        val nice = names.map { it to it.isNice() }
        assert(
            nice.filter { it.second }.count() == 2
        )
    }

    @Test
    fun part1() {
        val names = input.lines()
        val nice = names.map { it to it.isNice() }
        println(
            nice.filter { it.second }.count()
        )
    }

    @Test
    fun part2() {
        val names = input.lines()
        val nice = names.map { it to it.isNiceV2() }
        println(
            nice.filter { it.second }.count()
        )
    }

    private fun String.isNiceV2(): Boolean {
        return hasRepeatedPair() && hasRepeatWithOneBetween()
    }

    private fun String.hasRepeatWithOneBetween(): Boolean {
        return asSequence().filterIndexed { i, c -> c == getOrNull(i+2) }.firstOrNull() !=null
    }

    private fun String.hasRepeatedPair(): Boolean {
        return asSequence()
            .filterIndexed { i, c ->
                val target = "$c${getOrNull(i+1)}"
                drop(i+2).contains(target)
            }.firstOrNull() != null
    }

    private fun String.isNice(): Boolean {
        return hasAtleast3Vovels() &&
                hasRepeatedLetter() &&
                doesNotContainBlacklisted()
    }

    private fun String.hasRepeatedLetter(): Boolean {
        return windowed(2).any { it[0] == it[1] }
    }

    val blacklist = listOf("ab","cd","pq","xy")
    private fun String.doesNotContainBlacklisted(): Boolean {
        return blacklist.none { this.contains(it) }
    }

    val vovels = listOf('a', 'e', 'i', 'o', 'u')
    fun String.hasAtleast3Vovels(): Boolean {
        return filter{ it in vovels }.toList().size >=3
    }

}

