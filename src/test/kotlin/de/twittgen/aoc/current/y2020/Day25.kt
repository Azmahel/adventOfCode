package de.twittgen.aoc.current.y2020

import de.twittgen.aoc.current.Day
import java.lang.IllegalStateException

class Day25: Day<Pair<Long, Long>>() {
    override fun String.parse() = lines().map { it.toLong() }.let { (a,b) -> a to b }

    init {
        part1(14897079L, 7936032) {(p1, p2) ->
            val private1 = bruteForcePrivate(p1)
            p2.transform(private1)
        }
    }

    private val base = 20201227L
    private val subject = 7L

    private fun bruteForcePrivate(pubKey: Long): Int {
        var v = subject
        repeat(Int.MAX_VALUE) {
            if(v == pubKey) return it +1
            v *= subject
            v %= base
        }
        throw IllegalStateException()
    }


    private fun Long.transform(loopLength: Int): Long {
        var v = 1L
        repeat(loopLength) {
            v *= this
            v %= base
        }
        return v
    }

    override val example= """
        17807724
        5764801
    """.trimIndent()
}


