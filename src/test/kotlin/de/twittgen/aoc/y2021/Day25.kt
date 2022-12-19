package de.twittgen.aoc.y2021

import de.twittgen.aoc.Day
import de.twittgen.aoc.y2021.Day25.Colonies
import de.twittgen.aoc.util.Point2D

class Day25: Day<Colonies>() {
    override fun String.parse() = lines().reversed().flatMapIndexed { y, l ->  l.mapIndexed { x, c ->
        c to Cucumber(x,y)
    } }.groupBy(Pair<Char, Cucumber>::first, Pair<Char, Cucumber>::second)
        .let { Colonies(it['>']!!.toSet(), it['v']!!.toSet() , lines().first().length to lines().size) }

    init {
        part1(58,582) { it.runTillStable() }
        part2(0,0) {0} //free
    }

    data class Colonies(val rights: Set<Cucumber>, val downs: Set<Cucumber>, val dim : Pair<Int,Int>) {
        val all = rights + downs
        fun next(): Colonies = nextRight().nextDowns()
        private fun nextRight(): Colonies {
            val newRights = rights.map { c -> c.right().wrap(dim).let { n -> if (n in all)  c else n} }.toSet()
            return Colonies(newRights, downs, dim)
        }
        private fun nextDowns(): Colonies {
            val newDowns = downs.map { c -> c.down().wrap(dim).let { n -> if (n in all)  c else n} }.toSet()
            return Colonies(rights, newDowns, dim)
        }

        fun print() = (dim.second-1 downTo 0).joinToString("\n") { y -> (0 until dim.first).map { x ->
                if (rights.contains(Cucumber(x, y))) '>' else if (downs.contains(Cucumber(x, y))) 'v' else '.'
        }.joinToString("") }
    }

    private fun Colonies.runTillStable(): Int {
        var step = 1
        var previous = this
        var next = next()
        while(previous != next) {
            previous = next
            next = next.next()
            step ++
        }
        return step
        //return if (this == next)  step else next.runTillStable(step+1)
    }

    override val example = """
        v...>>.vv>
        .vv>>.vv..
        >>.>v>...v
        >>v>>.>.v.
        v>v.vv.v..
        >.>>..v...
        .vv..>.>v.
        v.v..>>v.v
        ....v..v.>
    """.trimIndent()
}
private typealias Cucumber = Point2D
private fun Cucumber.wrap(dim: Pair<Int,Int>) = Point2D(x.mod(dim.first), y.mod(dim.second))