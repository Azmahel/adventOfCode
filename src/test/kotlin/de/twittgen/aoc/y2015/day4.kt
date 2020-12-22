package de.twittgen.aoc.y2015

import de.twittgen.aoc.y2019.shared.util.FileUtil
import org.junit.jupiter.api.Test
import java.math.BigInteger
import java.security.MessageDigest

class day4 {
    val input = FileUtil.readInput("2015/day4")
        val examples = listOf(
            "abcdef",
            "pqrstuv"
        )
    @Test
    fun example() {
        val key = examples[0]
        val number = findHashNumber(key)
    }

    @Test
    fun part1() {
        val key = input
        val number = findHashNumber(key)
        println(number)
    }

    @Test
    fun part2() {
        val key = input
        val number = findHashNumber(key,6)
        println(number)
    }

    private fun findHashNumber(key: String, leadingLength:Int = 5): Int {
        return (0..Int.MAX_VALUE).asSequence().map {
            val md5 =(key+ it.toString()).md5()
            it to md5
        }.first { it.second.startsWith("".padStart(leadingLength,'0')) }.first
    }

    val md = MessageDigest.getInstance("MD5")
    fun String.md5(): String = BigInteger(1, md.digest(toByteArray())).toString(16).padStart(32, '0')
}