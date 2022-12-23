package de.twittgen.aoc.y2022

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.Point2D
import de.twittgen.aoc.util.cycle

class Day23: Day<Set<Elf>>() {
    override fun String.parse() =
        lines().flatMapIndexed { y, l -> l.mapIndexedNotNull { x, c -> if( c == '#') Elf(x,-y) else null } }.toSet()

    init {
        part1(110, 3780) { it.doRounds(10).first.score() }
        part2(20, 930){ it.doRounds().second }
    }

    private fun Set<Elf>.score() = ((maxOf{it.y} - minOf { it.y } +1) * (maxOf{it.x} - minOf { it.x }+1)) - size

    private fun Set<Elf>.doRounds(count: Int = Int.MAX_VALUE) : Pair<Set<Elf>, Int> {
        var current = this
        val priorities = mutableListOf(considerNorth,  considerSouth, considerWest, considerEast)
        repeat(count) {
            val next  = current.doRound(priorities).also { priorities.cycle() }
            if(next != current) current = next else return current to it+1
        }
        return current to count
    }

    private fun Set<Elf>.doRound(priorities: List<Consideration>) : Set<Elf> {
        val proposals = mapNotNull { elf -> if(elf.adjacent().any { it in this}) {
            elf.(priorities[0])(this) ?:
            elf.(priorities[1])(this) ?:
            elf.(priorities[2])(this) ?:
            elf.(priorities[3])(this)
        } else null }.groupBy { it.second }.filterValues { it.size == 1 }.values.flatten().toMap()
        return map { elf -> proposals[elf] ?: elf }.toSet()
    }

    private val considerNorth: Consideration = {other -> if(listOf(N, NE, NW).none { it in other }) this to N else null}
    private val considerEast:  Consideration = {other -> if(listOf(E, NE, SE).none { it in other }) this to E else null}
    private val considerSouth: Consideration = {other -> if(listOf(S, SW, SE).none { it in other }) this to S else null}
    private val considerWest:  Consideration = {other -> if(listOf(W, NW, SW).none { it in other }) this to W else null}

    override val example = """
      ....#..
      ..###.#
      #...#.#
      .#...##
      #.###..
      ##.#.##
      .#..#..
    """.trimIndent()
}

private typealias Elf = Point2D
private typealias Proposal = Pair<Elf, Elf>
private typealias Consideration = Elf.(Set<Elf>) -> Proposal?
private val Elf.N  get() = up()
private val Elf.NE  get() = up().right()
private val Elf.E  get() = right()
private val Elf.SE  get() = down().right()
private val Elf.S  get() = down()
private val Elf.SW  get() = down().left()
private val Elf.W  get() = left()
private val Elf.NW  get() = up().left()

