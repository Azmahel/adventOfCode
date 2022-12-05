package de.twittgen.aoc.y2022

import de.twittgen.aoc.Day
import java.util.*

typealias Ship = List<Stack<Char>>
class Day5 : Day<String, String, Pair<Ship, List<Day5.Instruction>>>() {
    override val example = """
    [D]    
[N] [C]    
[Z] [M] [P]
 1   2   3 

move 1 from 2 to 1
move 3 from 1 to 3
move 2 from 2 to 1
move 1 from 1 to 2    
    """.trimIndent()

    init {
        super.mutableModel = true
        part1("CMZ", "VCTFTJQCG") {
            second.forEach { first.performInstruction(it) }
            first.map { it.pop() }.joinToString("")
        }
        part2("MCD", "GCFGLDNJZ") {
            second.forEach { first.performInstructionV2(it) }
            first.map { it.pop() }.joinToString("")
        }
    }

    override fun String.parse(): Pair<Ship, List<Instruction>> = split("\n\n")
        .let { it[0].parseShip() to it[1].parseInstructions() }

    private fun Ship.performInstruction(instruction: Instruction) {
        repeat(instruction.amount) { get(instruction.to).push(get(instruction.from).pop()) }
    }
    private fun Ship.performInstructionV2(instruction: Instruction) {
        (1..instruction.amount).map { get(instruction.from).pop() }.reversed()
            .forEach { get(instruction.to).push(it) }
    }


    private fun String.parseShip(): Ship {
        val boxes = lines().dropLast(1).map { it.chunked(4).map(::parseBox) }
        val ship = (1..boxes.maxOf { it.size }).map { Stack<Char>() }
        boxes.reversed().forEach { it.forEachIndexed { row, box -> if(box != null) ship[row].push(box) } }
        return ship
    }

    private fun parseBox(s: String): Char? = Regex("\\[(.)]").find(s)?.groupValues?.get(1)?.single()

    private fun String.parseInstructions(): List<Instruction> = lines()
        .map {
            val (amount, from, to) = Regex("move (\\d+) from (\\d+) to (\\d+)").find(it)!!.destructured
            Instruction(amount.toInt(), from.toInt()-1, to.toInt()-1)
        }

    data class Instruction(val amount: Int, val from: Int, val to: Int)
}




