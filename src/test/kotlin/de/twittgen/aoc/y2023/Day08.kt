package de.twittgen.aoc.y2023

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.cycle
import de.twittgen.aoc.util.emptyLine
import de.twittgen.aoc.util.groups
import de.twittgen.aoc.util.lcm

class Day08: Day<Pair<List<Day08.Instruction>, Network>>() {
    override fun String.parse() = split(emptyLine).let { (a,b) ->
        a.map { it.toInstruction() } to b.lines().associate { nodeExp.groups(it)!!.let { (n, l, r) -> n to (l to r) } }
    }

    init {
        part1(null,16697) { (ins, network) -> "AAA".findZZZ(ins, network) }
        part2(6, 10668805667831) { (ins, network) -> network.findGhostways(ins) }
    }

    private fun Network.findGhostways(ins: List<Instruction>)=
        lcm(keys.filter { it.endsWith('A') }.map { it.findZZZ(ins, this, Regex("..Z")) })

    private tailrec fun String.findZZZ(
        ins: List<Instruction>,
        network: Network,
        target: Regex = Regex("ZZZ"),
        steps: Long = 0
    ) : Long {
        return if (target.matches(this)) steps
        else nextStep(ins, network).findZZZ(ins.cycle(), network, target, steps + 1)
    }

    private fun String.nextStep(ins: List<Instruction>, network: Network) = when(ins.first()) {
        Instruction.L -> network[this]!!.first
        Instruction.R -> network[this]!!.second
    }

    private val nodeExp = Regex("(.*) = \\((.*), (.*)\\)")
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

    enum class Instruction {
        L,R
    }
    private fun Char.toInstruction() = when(this) {
        'L' -> Instruction.L
        'R' -> Instruction.R
        else -> throw IllegalArgumentException()
    }
}
typealias Network = Map<String, Pair<String,String>>