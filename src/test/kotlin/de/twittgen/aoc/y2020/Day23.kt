package de.twittgen.aoc.y2020

import de.twittgen.aoc.Day
import de.twittgen.aoc.Day.TestType.SLOW
import de.twittgen.aoc.util.digitsToInt
import java.lang.Integer.min

class Day23 : Day<List<Int>>() {
    override val example = "389125467"
    override fun String.parse() = digitsToInt()

    init {
        part1(67384529, 47382659) { start ->
            playCupGame(start, 100).run {
                (dropWhile { it != 1 }.drop(1) + takeWhile { it!=1 }).joinToString("").toLong()
            }
        }
        part2(149245887792, 42271866720, SLOW) {
            playCupGameV2(it,1_000_000, 10_000_000)
        }
    }

    private fun playCupGameV2(cups: List<Int>, max: Int, moves: Int): Long {
        val next = cups.mapIndexed { i, it -> it to (cups.getOrNull(i + 1) ?: (i+2)) }.toMap().toMutableMap()
        next[max] = cups[0]
        fun Int.nextSmaller() = (this - 1).let { if (it == 0) max else it }
        fun Int.next() = next[this] ?: (this +1)
        var pivot = cups.first()
        repeat(moves) {
            val start =  pivot.next()
            val sandwich = start.next()
            val end = sandwich.next()
            val dropOut = listOf(start, sandwich, end)
            next[pivot] = end.next()

            val insertAfter = pivot.let {
                var i = it.nextSmaller()
                while (i in dropOut) { i = i.nextSmaller() }
                i
            }
            next[end] = insertAfter.next()
            next[insertAfter] = start
            pivot = next[pivot]!!
        }
        val x = next[1]!!
        val y = next[x]!!
        return x.toLong() * y.toLong()
    }

    private tailrec fun playCupGame(cups: List<Int>, moves: Int): List<Int> {
        if (moves <= 0) return  cups
        val pivot = cups.first()
        val pickUp = cups.take(4).drop(1)
        val inBetween =   cups.drop(4)
        val insertAt = inBetween.indexOf(min(inBetween.filter { it < pivot}.maxOrNull() ?: Int.MAX_VALUE, inBetween.maxOrNull()!!))
        val next = inBetween.take(insertAt+1) + pickUp + inBetween.drop(insertAt+1) + pivot
        return playCupGame(next, moves -1)
    }
}