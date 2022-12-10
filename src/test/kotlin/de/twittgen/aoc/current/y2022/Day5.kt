package de.twittgen.aoc.current.y2022

import de.twittgen.aoc.Day
import java.util.*

class Day5 : Day<Pair<Ship, List<Day5.Instruction>>>() {
    override fun String.parse(): Pair<Ship, List<Instruction>> = split("\n\n")
        .let { it[0].parseShip() to it[1].parseInstructions() }

    private fun String.parseShip(): Ship {
        val boxes = lines().dropLast(1).map { it.chunked(4).map(::parseBox) }
        val ship = (1..boxes.maxOf { it.size }).map { Stack<Char>() }
        boxes.reversed().forEach { it.forEachIndexed { row, box -> if(box != null) ship[row].push(box) } }
        return ship
    }

    private fun parseBox(s: String): Char? = Regex("\\[(.)]").find(s)?.groupValues?.get(1)?.single()

    private fun String.parseInstructions(): List<Instruction> = lines().map {
        val (amount, from, to) = Regex("move (\\d+) from (\\d+) to (\\d+)").find(it)!!.destructured
        Instruction(amount.toInt(), from.toInt()-1, to.toInt()-1)
    }

    init {
        super.mutableModel = true
        part1("CMZ", "VCTFTJQCG") { (ship, instructions) ->
            ship.apply {
                instructions.forEach { performInstruction(it) }
            }.tops.joinToString("")
        }
        part2("MCD", "GCFGLDNJZ") { (ship, instructions) ->
            ship.apply {
                instructions.forEach { performInstructionV2(it) }
            }.tops.joinToString("")
        }
    }

    private fun Ship.performInstruction(instruction: Instruction) =
        repeat(instruction.amount) { get(instruction.to).push(get(instruction.from).pop()) }

    private fun Ship.performInstructionV2(instruction: Instruction) =
        get(instruction).forEach { get(instruction.to).push(it) }


    data class Instruction(val amount: Int, val from: Int, val to: Int)
    private fun Ship.get(i: Instruction) = (1..i.amount).map { get(i.from).pop() }.reversed()

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
}
 private typealias Ship = List<Stack<Char>>
private val Ship.tops get() = map(Stack<Char>::pop)



