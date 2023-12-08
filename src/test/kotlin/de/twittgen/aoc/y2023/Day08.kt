package de.twittgen.aoc.y2023

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.cycle
import de.twittgen.aoc.util.emptyLine
import de.twittgen.aoc.util.groups
import de.twittgen.aoc.util.lcm

class Day08: Day<Pair<String, Network>>() {
    override fun String.parse() = split(emptyLine).let { (a,b) ->
        a to b.lines().associate { Regex("(.*) = \\((.*), (.*)\\)").groups(it)!!.let { (n, l, r) -> n to (l to r) } }
    }

    init {
        part1(null,16697) { (ins, network) -> "AAA".find(ins, network) }
        part2(6, 10668805667831) { (ins, network) -> network.findGhostways(ins) }
    }

    private fun Network.findGhostways(ins: String)=
        lcm(keys.filter { it.endsWith('A') }.map { it.find(ins, this, Regex("..Z")) })

    private tailrec fun String.find(ins: String, net: Network, to: Regex = Regex("ZZZ"), steps: Long = 0): Long =
        if (to.matches(this)) steps else nextStep(ins, net).find(ins.cycle(), net, to, steps + 1)

    private fun String.nextStep(ins: String, network: Network) = when(ins.first()) {
        'L' -> network[this]!!.first
        'R' -> network[this]!!.second
        else -> throw IllegalArgumentException()
    }

    override val example = """
        LR

        11A = (11B, XXX)
        11B = (XXX, 11Z)
        11Z = (11B, XXX)
        22A = (22B, XXX)
        22B = (22C, 22C)
        22C = (22Z, 22Z)
        22Z = (22B, 22B)
        XXX = (XXX, XXX)
    """.trimIndent()
}
private typealias Network = Map<String, Pair<String,String>>