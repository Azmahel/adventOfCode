package de.twittgen.aoc.old.y2020

import de.twittgen.aoc.util.second
import org.junit.jupiter.api.Test
import java.lang.IllegalStateException

class day25 {
    val input = """
        11349501
        5107328
    """.trimIndent()

    val example= """
        17807724
        5764801
    """.trimIndent()

    val base = 20201227L
    val subject = 7L


    fun parseInput(s:String) = s.lines().map { it.toLong() }

    @Test
    fun example() {
        val pubKeys = parseInput(example)
        val private1 = bruteForcePrivate(pubKeys.first())
        val ec =  pubKeys.second().transform(private1)
        assert(
            ec == 14897079L
        )
    }

    @Test
    fun part1() {
        val pubKeys = parseInput(input)
        val private1 = bruteForcePrivate(pubKeys.first())
        val ec =  pubKeys.second().transform(private1)
        println(ec)
    }

    private fun bruteForcePrivate(pubKey: Long): Int {
        var v = subject
        repeat(Int.MAX_VALUE) {
            if(v == pubKey) return it +1
            v *= subject
            v %= base

        }
        throw IllegalStateException()
    }
    private fun Long.transform(loopsize: Int): Long {
        var v = 1L
        repeat(loopsize) {
            v *= this
            v %= base
        }
        return v
    }
}


