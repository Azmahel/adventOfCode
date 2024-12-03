package de.twittgen.aoc.y2024

import de.twittgen.aoc.Day

class Day03 : Day<String>() {
    override fun String.parse() = this

    init {
        part1(161, 184511516) { l -> l.toMuls().sumOf(Mul::result) }
        part2(48, 90044227) { l -> l.toInstructions().run() }
    }

    private val mulsExpr = "mul\\((\\d+),(\\d+)\\)".toRegex()
    private fun String.toMuls() = mulsExpr.findAll(this)
        .map { r -> r.groupValues.let { Mul(it[1].toInt(), it[2].toInt()) } }
        .toList()
    private val instructionExpr = "(do)\\(\\)|(don't)\\(\\)|(mul)\\((\\d+),(\\d+)\\)".toRegex()
    private fun String.toInstructions() = instructionExpr.findAll(this)
        .map { r-> r.groupValues.let { when {
            it[1]!="" -> Do
            it[2]!="" -> Dont
            it[3]!="" -> Mul(it[4].toInt(), it[5].toInt())
            else -> throw IllegalArgumentException()
        } } }.toList()

    private fun List<Instruction>.run() = fold(State(true,0)) {s, i -> i.run(s)}.sum

    override val example = """
        xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))
    """.trimIndent()
}

private sealed class Instruction { abstract fun run(state: State): State }
private data class Mul(val left: Int, val right: Int) : Instruction() {
    fun result() = left * right
    override fun run(state: State) = if (state.enabled) state.copy(sum = state.sum + result()) else state
}
private data object Do : Instruction() { override fun run(state: State) = state.copy(enabled = true) }
private data object Dont : Instruction() { override fun run(state: State) = state.copy(enabled = false) }

private data class State(val enabled: Boolean, val sum: Int)