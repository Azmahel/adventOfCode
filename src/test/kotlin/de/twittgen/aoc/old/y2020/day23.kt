package de.twittgen.aoc.old.y2020

import org.junit.jupiter.api.Test
import java.lang.Integer.min

class day23 {
    val input = "364297581"
    val example = "389125467"

    fun parseInput(s: String ): List<Int> {
        return s.map { it.toString().toInt() }
    }

    @Test
    fun example() {
        val start = parseInput(example)
        val result = playCupGame(start, 10)
        val score = (result.dropWhile { it != 1 }.drop(1) + result.takeWhile { it!=1 }).joinToString("")
       assert(
           score == "92658374"
       )
    }

    @Test
    fun part1() {
        val start = parseInput(input)
        val result = playCupGame(start, 100)
        val score = (result.dropWhile { it != 1 }.drop(1) + result.takeWhile { it!=1 }).joinToString("")
        println(score)
    }

    @Test
    fun part2() {
        val start = parseInput(input).let {
            it+ ((it.maxOrNull()!!+1)..1_000_000)
        }
        val result = playCupGameV2(start, 10_000_000)
        println(result)
    }

    private fun playCupGameV2(cups:List<Int>, moves : Int) :Long {
        val current = cups.mapIndexed { i, it -> it to (cups.getOrNull(i + 1) ?: cups[0]) }.toMap().toMutableMap()
        fun Int.next() = current[this]!!
        infix fun Int.setNext(i: Int) { current[this] = i }

        val max = cups.maxOrNull()!!
        fun Int.nextSmaller() = (this-1).let { if (it == 0) max else it }

        var pivot = cups.first()
        repeat(moves) {
            val start = pivot.next()
            val sandwich = start.next()
            val end = sandwich.next()
            val dropOut = listOf(start, sandwich, end)

            pivot setNext end.next()

            val insertAfter = pivot.let {
                var i = it.nextSmaller()
                while (i in dropOut) { i = i.nextSmaller() }
                i
            }

            end setNext insertAfter.next()
            insertAfter setNext start
            pivot = pivot.next()
        }
        val x = 1.next()
        val y = x.next()
        return x.toLong() * y.toLong()
    }



    private tailrec fun playCupGame(cups: List<Int>, moves: Int): List<Int> {
        if (moves <= 0) return  cups
        val pivot = cups.first()
        val pickUp = cups.take(4).drop(1)
        val inBetween =   cups.drop(4)
        val insertAt = inBetween.indexOf(min(inBetween.filter { it < pivot}.maxOrNull() ?: Int.MAX_VALUE, inBetween.maxOrNull()!!))
        val next = inBetween.take(insertAt+1) + pickUp + inBetween.drop(insertAt+1) + pivot
        if(moves % 100_000 == 0) println(moves)
        return playCupGame(next, moves -1)
    }
}