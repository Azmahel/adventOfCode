package de.twittgen.aoc.y2023

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.groups

class Day15 : Day<List<String>>(){
    override fun String.parse() = lines().map { it.split(",") }.flatten()

    init {
        part1(1320, 517015) { it.sumOf { s ->  s.hash() } }
        part2(145, 286104) {it.map { it.toInstruction() }
            .toHashMap().entries.sumOf { (b, l) -> l.mapIndexed { p, (_,f) -> (b+1)*(p+1)*f  }.sum() }
        }
    }
    private fun List<Instruction>.toHashMap() : Map<Long, List<Pair<String, Int>>> {
        val current = hashMapOf<Long,List<Pair<String,Int>>>()
        forEach { ins ->
            val hash = ins.label.hash()
            if( ins.operation == '-') {
                current[hash] = current[hash]?.toMutableList()
                    ?.also { l -> l.findByLabel(ins.label)?.let { i -> l.removeAt(i) } } ?: emptyList()
            } else {
                current[hash] = current[hash]?.toMutableList()
                    ?.also { l -> l.findByLabel(ins.label)?.let { i ->
                        l.set(i, ins.label to ins.focal!!) } ?: l.add(ins.label to ins.focal!!)
                    } ?: listOf(ins.label to ins.focal!!)
            }
        }
        return current
    }

    private fun List<Pair<String, Int>>.findByLabel(s: String) = indexOfFirst { it.first == s }.let { if(it == -1) null else it }
    private fun String.hash() = fold(0L) { i ,c -> c.hash(i) }

    private val ins = Regex("(.*)([=\\-])(\\d*)")
    private fun String.toInstruction() = ins.groups(this)!!.let { (a, b, c) -> Instruction(a,b.first(),c.toIntOrNull()) }
    data class Instruction(val label: String, val operation: Char, val focal: Int?)

    private fun Char.hash(current: Long =0) : Long = ((current + code) * 17) % 256

    override val example = """
        rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7
    """.trimIndent()
}

