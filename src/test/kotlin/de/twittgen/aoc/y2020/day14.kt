package de.twittgen.aoc.y2020

import de.twittgen.aoc.util.FileUtil
import de.twittgen.aoc.util.second
import de.twittgen.aoc.y2020.day14.Instruction.Mask
import de.twittgen.aoc.y2020.day14.Instruction.Mem
import org.junit.jupiter.api.Test
import java.lang.IllegalArgumentException

typealias BitNumber = List<Char?>
class day14 {
    val input = FileUtil.readInput("2020/day14")
    val example = """
       mask = XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X
       mem[8] = 11
       mem[7] = 101
       mem[8] = 0 
    """.trimIndent()

    val example2 = """
        mask = 000000000000000000000000000000X1001X
        mem[42] = 100
        mask = 00000000000000000000000000000000X0XX
        mem[26] = 1
    """.trimIndent()

    val memOp = Regex("mem\\[(\\d+)]")
    fun parseInput(s:String): List<Instruction> {
        return s.lines().map {
            with(it.split(" = ")) {
                when (first()) {
                    "mask" -> Mask(
                        second().reversed().map { if (it == 'X') null else it }
                    )
                    else -> {
                        val (addr) = memOp.matchEntire(first())!!.destructured
                        Mem(addr.toLong(), second().toBitNumber())
                    }
                }
            }
        }

    }

    @Test
    fun example() {
        val instructions = parseInput(example)
        val ferry = Ferry()
        ferry(instructions)
        assert(
            ferry.memory[8]!!.toLong() == 64L
        )
        assert(
            ferry.memory[7]!!.toLong() == 101L
        )
    }

    @Test
    fun part1() {
        val instructions = parseInput(input)
        val ferry = Ferry()
        ferry(instructions)
        println(
            ferry.memory.values.fold(0L){c, v -> c + v.toLong()}
        )
    }

    @Test
    fun part2() {
        val instructions = parseInput(input)
        val ferry = FerryV2()
        ferry(instructions)
        println(
            ferry.memory.values.fold(0L){c, v -> c + v.toLong()}
        )
    }

    sealed class Instruction {
        class Mask(val value: BitNumber) : Instruction()
        class Mem(val addr: Long, val value: BitNumber) : Instruction()
    }

    class FerryV2: Ferry() {
        override fun write(op: Mem) {
            val addresses = getfloatyAdresses(op.addr.toBitNumber())
            addresses.forEach {
                memory[it.toLong()] = op.value
            }
        }

        fun getfloatyAdresses(base:BitNumber): List<BitNumber>{
            return mask.foldIndexed(listOf(emptyList())) {
                    i, current , bit ->
                when(bit) {
                    '0' -> current.map { it + (base.getOrNull(i) ?: '0') }
                    '1' -> current.map { it+ '1' }
                    null -> current.flatMap { listOf(it +'1', it + '0') }
                    else -> throw IllegalArgumentException()
                }
            }
        }
    }

    open class Ferry {
        var mask : BitNumber = emptyList()
        val memory = mutableMapOf<Long,BitNumber>()
        protected open fun write(op: Mem) {
            memory[op.addr] = mask.mapIndexed {i, mBit ->
                mBit?: op.value.getOrNull(i) ?: '0'
            }
        }
        operator fun invoke(instructions: List<Instruction>) {
            for(op in instructions) {
                when(op) {
                    is Mask -> mask = op.value
                    is Mem -> write(op)
                }
                //println(memory.mapValues { (_, value) -> value.toLong() })
            }
        }
    }
}
fun Long.toBitNumber() = toString(2).toList().reversed()
fun String.toBitNumber() = trim().toInt().toString(2).toList().reversed()
fun BitNumber.toLong() = reversed().joinToString("").toLong(2)