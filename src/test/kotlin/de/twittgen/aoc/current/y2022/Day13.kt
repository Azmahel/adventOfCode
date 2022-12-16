package de.twittgen.aoc.current.y2022

import de.twittgen.aoc.current.Day
import de.twittgen.aoc.util.NestedList
import de.twittgen.aoc.util.NestedList.Nested
import de.twittgen.aoc.util.NestedList.Terminal
import de.twittgen.aoc.util.toNestedList

class Day13 : Day<List<Pair<NestedList, NestedList>>>() {
    override fun String.parse() = replace('[', '<').replace(']', '>')
        .split("\n\n").map { pair -> pair.lines().map { it.toNestedList() }.let { (a, b) -> a to b } }

    init {
        part1(13,) { it
            .mapIndexed { i, p -> i+1 to p.first.isSmallerThan(p.second) }
            .filter { (_,p) ->  p < 0 }
            .sumOf { (i,_) -> i }
        }
        part2(140,) { it
            .flatMap { (a,b) -> listOf(a,b) }
            .plus(divider2).plus(divider6)
            .sortedWith { a,b -> a.isSmallerThan(b) }
            .run { (indexOf(divider2)+1) * (indexOf(divider6)+1) }
        }
    }

    private val divider2 = "<<2>>".toNestedList()
    private val divider6 = "<<6>>".toNestedList()
    private fun NestedList.isSmallerThan(other: NestedList) : Int {
        return if( this is Terminal && other is Terminal ) {
            value - other.value
        } else if( this is Nested && other is Nested ) {
            content.zip(other.content).map { (a, b) -> a.isSmallerThan(b) }.firstOrNull { it != 0 }
                ?: (content.size - other.content.size)
        } else {
            if(this is Terminal) Nested(listOf(this)).isSmallerThan(other) else this.isSmallerThan(Nested(listOf(other)))
        }
    }
    override val example ="""
        [1,1,3,1,1]
        [1,1,5,1,1]

        [[1],[2,3,4]]
        [[1],4]

        [9]
        [[8,7,6]]

        [[4,4],4,4]
        [[4,4],4,4,4]

        [7,7,7,7]
        [7,7,7]

        []
        [3]

        [[[]]]
        [[]]

        [1,[2,[3,[4,[5,6,7]]]],8,9]
        [1,[2,[3,[4,[5,6,0]]]],8,9]
    """.trimIndent()
}