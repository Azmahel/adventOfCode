package de.twittgen.aoc.y2021

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.second
import de.twittgen.aoc.y2021.Day16.Packet
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.lang.IllegalStateException
import java.lang.Integer.min

class Day16 : Day<Int, Long, Packet> (){

    override val example = """8A004A801A8002F478"""

    override fun String.parse() = map { Integer.toBinaryString(it.toString().toInt(16)).padStart(4,'0') }
            .joinToString("").let { BitStream(it).decodePacket() }

    init {
        part1(16, 891) { versionScore() }
        part2(15, 673042777597) { score() }
    }

    private fun BitStream.decodePacket(): Packet {
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
            while(packetBits.hasNext()) { packets.add(packetBits.decodePacket()) }
        } else {
            val packetCount = readBits(11).toInt(2)
            repeat(packetCount) { packets.add(decodePacket()) }
        }
        return packets
    }

    private fun BitStream.decodeLiteral(): Long {
        var number = ""
        while(readBits(1) == "1"){ number += readBits(4) }
        number += readBits(4)

        return number.toLong(2)
    }

    class BitStream(private val binaryString: String) {
        private var pointer = 0
        fun readBits(count: Int) = binaryString
            .substring(pointer, min(pointer +count, binaryString.length))
            .also { pointer += count }
        fun hasNext() = pointer < binaryString.lastIndex
    }

    private fun Packet.versionScore() : Int = when(this) {
        is Literal -> version
        is Operator -> version + packets.sumOf { it.versionScore() }
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
}



