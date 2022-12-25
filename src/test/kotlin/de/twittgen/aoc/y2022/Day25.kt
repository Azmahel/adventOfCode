package de.twittgen.aoc.y2022

import de.twittgen.aoc.Day
import kotlin.math.pow

class Day25: Day<List<Snafu>>() {
    override fun String.parse() = lines().map {l ->  l.map { mapping[it]!! } }

    init {
        part1("2=-1=0", "2-0-020-1==1021=--01") { it.sumOf {snafu ->  snafu.toDecimal() }.toSnafu() }
        part2(0, 0) { 0 } //free Star!
    }

    private fun Snafu.toDecimal() = reversed().mapIndexed{ i, x -> x* (5.0).pow(i).toLong() }.sum()
    private fun Long.toSnafu(): String {
        var (current, carry, remaining)  = Triple("",0,this)
        while (remaining != 0L) {
            var mod = (remaining % 5).toInt() + carry
            if(mod >= 3) mod = -5+mod.also { carry = 1 } else carry = 0
            current += reverseMapping[mod]!!
            remaining /= 5
        }
        return current.reversed()
    }

    private val mapping = mapOf('=' to -2 , '-' to -1 , '0' to 0, '1' to 1, '2' to 2)
    private val reverseMapping = mapping.toList().associateBy({ it.second }, { it.first})
    override val example = """
        1=-0-2
        12111
        2=0=
        21
        2=01
        111
        20012
        112
        1=-1=
        1-12
        12
        1=
        122
    """.trimIndent()
}
private typealias Snafu = List<Int>