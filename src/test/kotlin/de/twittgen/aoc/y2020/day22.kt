package de.twittgen.aoc.y2020

import de.twittgen.aoc.y2019.shared.util.FileUtil
import de.twittgen.aoc.y2019.shared.util.second
import org.junit.jupiter.api.Test

class day22 {
    val input = FileUtil.readInput("2020/day22")
    val example = """
        Player 1:
        9
        2
        6
        3
        1

        Player 2:
        5
        8
        4
        7
        10
    """.trimIndent()

    val example2 = """
        Player 1:
        43
        19
        
        Player 2:
        2
        29
        14
    """.trimIndent()

    @Test
    fun example() {
        val (you, crab) = parseInput(example)
        val (you2, crab2) = playCombat(you,crab)
        val winner = listOf(you2,crab2).first { it.isNotEmpty() }
        assert(
            winner.score() == 306
        )
    }

    @Test
    fun part1() {
        val (you, crab) = parseInput(input)
        val (you2, crab2) = playCombat(you,crab)
        val winner = listOf(you2,crab2).first { it.isNotEmpty() }
        println(winner.score())
    }

    @Test
    fun part2() {
        val (you, crab) = parseInput(input)
        val (you2, crab2) = playRecursiveCombat(you,crab)
        val winner = listOf(you2,crab2).first { it.isNotEmpty() }
        println(winner.score())
    }

    fun List<Int>.score() = reversed().mapIndexed { i, v -> (i+1) * v  }.sum()

    private tailrec fun playRecursiveCombat(
        p1: List<Int>,
        p2: List<Int>,
        previous: Set<Pair<List<Int>, List<Int>>> = emptySet()
    ): Pair<List<Int>, List<Int>> {
        if(p1.isEmpty() || p2.isEmpty()) return p1 to p2
        if(p1 to p2 in previous) return p1 to emptyList()

        val (c1, c2) = p1.first() to p2.first()
        val (p1Next, p2Next) = p1.drop(1).toMutableList() to p2.drop(1).toMutableList()
        val p1Wins = if(p1Next.size >= c1  && p2Next.size >= c2) {
            playRecursiveCombat(p1Next.take(c1), p2Next.take(c2))
                .first.isNotEmpty()
        }else  { c1 > c2 }
        if(p1Wins) p1Next.addAll(listOf(c1,c2)) else p2Next.addAll(listOf(c2,c1))
        return playRecursiveCombat(p1Next, p2Next, previous + (p1 to p2))
    }

    private tailrec fun playCombat(p1: List<Int>, p2: List<Int>): Pair<List<Int>,List<Int>> {
        if(p1.isEmpty() || p2.isEmpty()) return p1 to p2
        val (c1, c2) = p1.first() to p2.first()
        val (p1Next, p2Next) = p1.drop(1).toMutableList() to p2.drop(1).toMutableList()
        if (c1 > c2) p1Next.addAll(listOf(c1,c2)) else p2Next.addAll(listOf(c2,c1))
        return playCombat(p1Next, p2Next)
    }

    fun parseInput(s: String) : Pair<List<Int>,List<Int>> {
       return s
           .replace("\r","")
           .split("\n\n")
           .map {
               it.lines().drop(1).map { it.toInt() }
           }.let {
               it.first() to it.second()
           }
    }
}