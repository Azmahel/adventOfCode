package de.twittgen.aoc.y2021

import de.twittgen.aoc.util.FileUtil
import de.twittgen.aoc.util.second
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.lang.IllegalStateException
import java.lang.Integer.min

class Day16 {
    val input by lazy { FileUtil.readInput("2021/day16").parse() }
    val example = """D2FE28""".parse()

    private fun String.parse() =
        BitStream(
            map { Integer.toBinaryString(it.toString().toInt(16)).padStart(4,'0') }.joinToString("")
        )

    class BitStream(private val binaryString: String) {
        private var pointer = 0

        fun readBits(count: Int) = binaryString
            .substring(pointer, min(pointer +count, binaryString.length))
            .also { pointer += count }

        fun hasNext() = pointer < binaryString.lastIndex
        val current get() = pointer to binaryString[pointer]
        fun asString() = binaryString.substring(0,pointer)+
                "." +
                binaryString[pointer] +
                "." +
                binaryString.substring(pointer,binaryString.lastIndex)
    }



    sealed class Packet(open val version: Int) { abstract fun score() : Long }
    sealed class Operator(override val version: Int, open val packets: List<Packet>): Packet(version)

    data class Literal(override val version:Int, val value: Long): Packet(version) {
        override fun score(): Long = value
    }
    data class Sum(override val version: Int, override val packets: List<Packet>): Operator(version,packets) {
        override fun score() = packets.sumOf { it.score() }
    }
    data class Prod(override val version: Int, override val packets: List<Packet>): Operator(version,packets) {
        override fun score() = packets.fold(1L) { i, it ->  it.score() *i }
    }
    data class Min(override val version: Int, override val packets: List<Packet>): Operator(version,packets) {
        override fun score() = packets.minOf { it.score() }
    }
    data class Max(override val version: Int, override val packets: List<Packet>): Operator(version,packets) {
        override fun score() = packets.maxOf { it.score() }
    }
    data class GT(override val version: Int, override val packets: List<Packet>): Operator(version,packets) {
        override fun score() = if (packets.first().score() > packets.second().score()) 1L else 0L
    }

    data class LT(override val version: Int, override val packets: List<Packet>): Operator(version,packets) {
        override fun score() = if (packets.first().score() < packets.second().score()) 1L else 0L
    }

    data class Equal(override val version: Int, override val packets: List<Packet>): Operator(version,packets) {
        override fun score() = if (packets.first().score() ==  packets.second().score()) 1L else 0L
    }

    private fun Packet.versionScore() : Int = when(this) {
        is Literal -> version
        is Operator -> version + packets.sumOf { it.versionScore() }
    }

    private fun BitStream.decode(): Packet {
        val version = readBits(3).toInt(2)
        return when(readBits(3).toInt(2)) {
            4 -> Literal(version, decodeLiteral())
            0 -> Sum(version, decodeOperator())
            1 -> Prod(version, decodeOperator())
            2 -> Min(version, decodeOperator())
            3 -> Max(version, decodeOperator())
            5 -> GT(version, decodeOperator())
            6 -> LT(version, decodeOperator())
            7 -> Equal(version, decodeOperator())
            else -> throw IllegalStateException()
        }

    }

    private fun BitStream.decodeOperator(): List<Packet> {
        val lengthId= readBits(1)
        val packets = mutableListOf<Packet>()
        if(lengthId == "0") {
            val bitCount = readBits(15).toInt(2)
            val packetBits = BitStream(readBits(bitCount))
            while(packetBits.hasNext()) {
                packets.add(packetBits.decode())
            }
        } else {
            val packetCount = readBits(11).toInt(2)
            (1..packetCount).forEach { _ ->
                packets.add(decode())
            }
        }
        return packets
    }



    private fun BitStream.decodeLiteral(): Long {
        var number = ""
        while(readBits(1) == "1"){
            number += readBits(4)
        }
        number += readBits(4)

        return number.toLong(2)
    }

    @Test
    fun example() {
        assertEquals(Literal(6,2021), "D2FE28".parse().decode())
        assertEquals(
            Sum(1,listOf(
                Literal(6,10),
                Literal(2,20)
            )), "38006F45291200".parse().decode()
        )
        assertEquals(
            Sum(7,listOf(
                Literal(2,1),
                Literal(4,2),
                Literal(1,3)
            )), "EE00D40C823060".parse().decode()
        )
        assertEquals(16, "8A004A801A8002F478".parse().decode().score())
    }

    @Test
    fun example2() {
        assertEquals(3, "C200B40A82".parse().decode().score())
        assertEquals(54, "04005AC33890".parse().decode().score())
        assertEquals(7, "880086C3E88112".parse().decode().score())
        assertEquals(9, "CE00C43D881120".parse().decode().score())
        assertEquals(1, "D8005AC2A8F0".parse().decode().score())
        assertEquals(0, "F600BC2D8F".parse().decode().score())
        assertEquals(0, "9C005AC2F8F0".parse().decode().score())
        assertEquals(1, "9C0141080250320F1802104A08".parse().decode().score())
    }

    @Test
    fun part1() {
        val result =  input.decode().versionScore()
        println(result)
    }

    @Test
    fun part2() {
        val result =  input.decode().score()
        println(result)
    }
}



