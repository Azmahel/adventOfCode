package de.twittgen.aoc.y2020

import de.twittgen.aoc.Day
import de.twittgen.aoc.y2020.Day14.Instruction
import de.twittgen.aoc.y2020.Day14.Instruction.Mask
import de.twittgen.aoc.y2020.Day14.Instruction.Mem
import java.lang.IllegalArgumentException

class Day14 : Day<List<Instruction>>() {
    private val memOp = Regex("mem\\[(\\d+)]")
    override fun String.parse() = lines().map { l ->  l.split(" = ").let { (op, v) -> when (op) {
        "mask" -> Mask(v.reversed().map { if (it == 'X') null else it })
        else -> memOp.matchEntire(op)!!.destructured.let { (a) ->   Mem(a.toLong(), v.toBitNumber())}
    } } }

    init {
        part1(51, 8471403462063) { Ferry()(it).memory.values.fold(0L){ c, v -> c + v.toLong() } }
        part2(208, 2667858637669) { FerryV2()(it).memory.values.fold(0L){ c, v -> c + v.toLong() }}
    }

    sealed class Instruction {
        class Mask(val value: BitNumber) : Instruction()
        class Mem(val a: Long, val value: BitNumber) : Instruction()
    }

    class FerryV2: Ferry() {
        override fun write(op: Mem) = floatyAddresses(op.a.toBitNumber()).forEach { memory[it.toLong()] = op.value }

        private fun floatyAddresses(base: BitNumber): List<BitNumber> =
            mask.foldIndexed(listOf(emptyList())) { i, current , bit -> when(bit) {
                '0' -> current.map { it + (base.getOrNull(i) ?: '0') }
                '1' -> current.map { it+ '1' }
                null -> current.flatMap { listOf(it +'1', it + '0') }
                else -> throw IllegalArgumentException()
            } }
    }

    open class Ferry {
        var mask : BitNumber = emptyList()
        val memory = mutableMapOf<Long, BitNumber>()
        protected open fun write(op: Mem) {
            memory[op.a] = mask.mapIndexed { i, mBit -> mBit?: op.value.getOrNull(i) ?: '0' }
        }

        operator fun invoke(instructions: List<Instruction>): Ferry {
            for(op in instructions) { when(op) {
                is Mask -> mask = op.value
                is Mem -> write(op)
            } }
            return this
        }
    }

    override val example = """
       mask = 000000000000000000000000000000X1001X
       mem[42] = 100
       mask = 00000000000000000000000000000000X0XX
       mem[26] = 1
    """.trimIndent()
}
fun Long.toBitNumber() = toString(2).toList().reversed()
fun String.toBitNumber() = trim().toInt().toString(2).toList().reversed()
fun BitNumber.toLong() = reversed().joinToString("").toLong(2)
private typealias BitNumber = List<Char?>
