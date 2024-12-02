package de.twittgen.aoc.y2020

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.groups
import de.twittgen.aoc.util.mapLines
import de.twittgen.aoc.util.replaceAt

private typealias Program = List<Pair<String, Int>>
class Day08: Day<Program>() {

    override fun String.parse() = mapLines {
        Regex("([a-z]+) ([+\\-][0-9]+)").groups(it)!!.let { (op, v) -> op to v.toInt()}
    }

    init {
        part1(5, 1446) { it.runUntilDuplicate().first }
        part2(8, 1403) { p -> p.forEachIndexed { i ,ins -> when(ins.first) {
            "jmp" -> "nop" to ins.second
            "nop" -> "jmp" to ins.second
            else -> null
        }?.let {v -> p.toMutableList().replaceAt(i,v).runUntilDuplicate().let { (acc, ins) ->
            if (ins.last() == p.lastIndex) return@part2 acc
        } } } }
    }

    private fun Program.runUntilDuplicate(): Pair<Int,List<Int>> {
        var ip =0
        var acc = 0
        val visited = mutableListOf<Int>()
        while (ip !in visited && ip < size) {
            visited += ip
            val (operation, value) = this[ip]
            when(operation) {
                "nop" -> ip ++
                "jmp" -> ip += value
                "acc" -> acc += value.also { ip ++ }
            }
        }
        return acc to visited
    }
    override val example = """
        nop +0
        acc +1
        jmp +4
        acc +3
        jmp -3
        acc -99
        acc +1
        jmp -4
        acc +6
    """.trimIndent()
}