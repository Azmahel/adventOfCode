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
        part2(149245887792, 42271866720) {
            playCupGameV2(it,1_000_000, 10_000_000)
        }
    }

    private fun playCupGameV2(cups: List<Int>, max: Int, moves: Int): Long {
        val next = IntArray(max+1) { if(it==max) cups.first() else it +1 }
        cups.forEachIndexed { i, it -> next[it] = (cups.getOrNull(i + 1) ?: (i+2)) }
        fun Int.nextSmaller() = (this - 1).let { if (it == 0) max else it }
        var pivot = cups.first()
        repeat(moves) {
            val start = next[pivot]
            val sandwich = next[start]
            val end = next[sandwich]
            val dropOut = listOf(start, sandwich, end)
            next[pivot] = next[end]

            val insertAfter = pivot.let {
                var i = it.nextSmaller()
                while (i in dropOut) {
                    i = i.nextSmaller()
                }
                i
            }
            next[end] = next[insertAfter]
            next[insertAfter] = start
            pivot = next[pivot]
        }
        val x = next[1]
        val y = next[x]
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