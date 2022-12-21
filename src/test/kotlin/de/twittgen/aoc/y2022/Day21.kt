package de.twittgen.aoc.y2022

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.isNumber
import de.twittgen.aoc.y2022.Day21.Monkey
import java.lang.IllegalStateException
import kotlin.math.abs
import kotlin.math.sign


class Day21 : Day<Map<String, Monkey>>() {
    override fun String.parse() = lines().associate { m -> m.split(": ").let { (name, ins) ->
        name to parseInst(ins)
    } }

    private val opEx = Regex("([a-z]+) ([+\\-*/]) ([a-z]+)")
    private fun parseInst(s: String) = if(s.isNumber()) NumMonkey(s) else parseOp(s)
    private fun parseOp(s:String) = opEx.matchEntire(s)!!.destructured.let { (a,op,b) -> when(op) {
        "+" -> AddMonkey(a,b)
        "*" -> MulMonkey(a,b)
        "-" -> SubMonkey(a,b)
        "/" -> DivMonkey(a,b)
        else -> throw IllegalStateException()
    } }

    init {
        part1(152, 170237589447588) { it["root"]!!.scream(it)(0) }
        part2(301, 3712643961892) {
            val newRoot = it["root"]!!.run { EqlMonkey(a, b) }
            val func = newRoot.scream(it + ("humn" to Human()))
            val sign = (func(1) - func(10)).sign
            var (min, max, pivot) = listOf(0L, Int.MAX_VALUE * 2000L, Int.MAX_VALUE * 1000L)
            while (true) {
                val result = func(pivot)
                if (result == 0L) return@part2 pivot
                if (pivot == max) max += Int.MAX_VALUE * 2000L
                if (pivot == min) min -= Int.MAX_VALUE * 2000L

                if (result.sign == sign) {
                    min = pivot.also { pivot += abs(max - pivot) / 2 }
                } else {
                    max = pivot.also { pivot -= abs(pivot - min) / 2 }
                }
            }
        }
    }

    sealed class Monkey(val a: String, val b: String) {
        abstract fun scream(m: Map<String,Monkey>) : Computation
    }

    abstract class OpMonkey(a: String, b:String) : Monkey(a,b){
        override fun scream(m: Map<String,Monkey>) = invoke((m[a]!!.scream(m) to m[b]!!.scream(m)))
        protected abstract val  invoke : (Pair<Computation,Computation>) -> Computation
    }

    class AddMonkey(a: String, b: String) : OpMonkey(a, b) {
        override val  invoke: (Pair<Computation,Computation>) -> Computation = { (a,b) -> { a(it) + b(it) } }
    }

    class SubMonkey(a: String, b: String) : OpMonkey(a, b) {
        override val  invoke: (Pair<Computation,Computation>) -> Computation = { (a,b) -> { a(it) - b(it) } }
    }

    class MulMonkey(a: String, b: String) : OpMonkey(a, b) {
        override val  invoke: (Pair<Computation,Computation>) -> Computation = { (a,b) -> { a(it) * b(it) } }
    }

    class DivMonkey(a: String, b: String) : OpMonkey(a, b) {
        override val  invoke: (Pair<Computation,Computation>) -> Computation = { (a,b) -> { a(it) / b(it) } }
    }

    class NumMonkey(a : String) : Monkey(a, "") {
        override fun scream(m: Map<String, Monkey>): Computation = { a.toLong() }
    }

    class Human : Monkey("", "") { override fun scream(m: Map<String, Monkey>): Computation = { it } }
    class EqlMonkey(a: String, b: String) : OpMonkey(a, b) {
        override val  invoke: (Pair<Computation,Computation>) -> Computation = { (a,b) -> { b(it)- a(it) } }
    }

    override val example = """
        root: pppw + sjmn
        dbpl: 5
        cczh: sllz + lgvd
        zczc: 2
        ptdq: humn - dvpt
        dvpt: 3
        lfqf: 4
        humn: 5
        ljgn: 2
        sjmn: drzm * dbpl
        sllz: 4
        pppw: cczh / lfqf
        lgvd: ljgn * ptdq
        drzm: hmdt - zczc
        hmdt: 32
    """.trimIndent()
}
private typealias Computation = (Long) -> Long