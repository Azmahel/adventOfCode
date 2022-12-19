package de.twittgen.aoc.y2020

import de.twittgen.aoc.Day

class Day22 : Day<Pair<Deck, Deck>>(){
    override fun String.parse() = split("\n\n")
        .map { it.lines().drop(1).map {c ->  c.toInt() } }.let { (p1, p2) -> p1 to p2 }

    init {
        part1(306, 32677) { (you, crab) ->
            playCombat(you.toMutableList(), crab.toMutableList()).toList().first { it.isNotEmpty() }.score()
        }
        part2(291, 33661) { (you, crab) ->
            playCombat(you.toMutableList(), crab.toMutableList(), true).toList().first { it.isNotEmpty() }.score()
        }
    }

    private fun List<Int>.score() = reversed().mapIndexed { i, v -> (i+1) * v  }.sum()

    private tailrec fun playCombat(
        p1: MutableList<Int>,
        p2: MutableList<Int>,
        recursive: Boolean = false,
        previous: MutableSet<Pair<List<Int>, List<Int>>> = mutableSetOf()
    ): Pair<List<Int>, List<Int>> {
        if(p1.isEmpty() || p2.isEmpty()) return p1 to p2
        if(p1 to p2 in previous && recursive) return p1 to emptyList()
        previous += (p1.toList() to p2.toList())
        val (c1, c2) = p1.removeAt(0) to p2.removeAt(0)
        val p1Wins = if(recursive && p1.size >= c1  && p2.size >= c2)  {
            playCombat(p1.take(c1).toMutableList(), p2.take(c2).toMutableList(), recursive).first.isNotEmpty()
        } else  {
            c1 > c2
        }
        if(p1Wins) p1.addAll(listOf(c1,c2)) else p2.addAll(listOf(c2,c1))
        return playCombat(p1, p2, recursive, previous)
    }

    override val example = """
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
}
private typealias Deck = List<Int>