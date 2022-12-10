package de.twittgen.aoc.old.y2020

import de.twittgen.aoc.util.FileUtil
import org.junit.jupiter.api.Test

 private typealias Program = List<Pair<String, Int>>
class day8 {
    val input = FileUtil.readInput("2020/day8")
    val example = """
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

    fun parseInput(s: String): Program {
         return s.lines().map {
            val (operation, value) = Regex("([a-z]+) ((?:\\+|-)[0-9]+)")
                .matchEntire(it)!!.destructured
            operation to value.toInt()
        }

    }

    @Test
    fun example() {
        val program = parseInput(example)
        val (result) = program.runUntilDuplicate()
        assert(result == 5)
    }

    @Test
    fun part1() {
        val program = parseInput(input)
        val result = program.runUntilDuplicate()
        println(result)
    }

    @Test
    fun part2() {
        val program = parseInput(input)
        program.forEachIndexed { index, instruction ->
            val replacement = when(instruction.first) {
                "jmp" -> "nop" to instruction.second
                "nop" -> "jmp" to instruction.second
                else -> null
            }
            replacement?.let{
                val changedProgram = program.toMutableList().apply {
                    removeAt(index)
                    add(index, it)
                }
                val (acc, instructions) = changedProgram.runUntilDuplicate()
                if (instructions.last() == changedProgram.lastIndex) {
                    println(acc)
                    return
                }

            }

        }

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
                "acc" -> {
                    ip++
                    acc += value
                }
            }
        }
        return acc to visited
    }

}