package de.twittgen.aoc.y2022

import de.twittgen.aoc.Day
import de.twittgen.aoc.Day.TestState.EXAMPLE
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
        part1(152, 170237589447588) { it["root"]!!.scream(it)() }
        part2(301, 3712643961892) {
            val newRoot = it["root"]!!.run { EqlMonkey(a,b) }
            val you = Human()
            val func =  newRoot.scream(it + ("humn" to you))
            // this is a bit hacky to prevent sign detection and long overflow prevention
            val sign = if(EXAMPLE == testState) 1 else -1
            var min = 0L
            var max = Int.MAX_VALUE*2000L
            var pivot = Int.MAX_VALUE*1000L
            while (true) {
                you.i = pivot
                func().let { result ->
                    if(result == 0L) return@part2 pivot
                    if(result.sign == sign) {
                        min = pivot
                        pivot += abs(max-pivot) /2
                    } else {
                        max = pivot
                        pivot -= abs(pivot - min) / 2
                    }
                }
            }
        }
    }

    sealed class Monkey(val a: String, val b: String) {
        abstract fun scream(m: Map<String,Monkey>) : () -> Long
    }
    class AddMonkey(a: String, b: String) : Monkey(a, b) {
       override fun scream(m : Map<String, Monkey>) : () -> Long {
           val a = m[a]!!.scream(m)
           val b = m[b]!!.scream(m)
           return { a() + b() }
       }
    }
    class SubMonkey(a: String, b: String) : Monkey(a, b) {
        override fun scream(m : Map<String, Monkey>): () -> Long {
            val a = m[a]!!.scream(m)
            val b = m[b]!!.scream(m)
            return { a() - b() }
        }
    }
    class MulMonkey(a: String, b: String) : Monkey(a, b) {
        override fun scream(m : Map<String, Monkey>): () -> Long {
            val a = m[a]!!.scream(m)
            val b = m[b]!!.scream(m)
            return { a() * b() }
        }
    }
    class DivMonkey(a: String, b: String) : Monkey(a, b) {
        override fun scream(m : Map<String, Monkey>) : () -> Long {
            val a = m[a]!!.scream(m)
            val b = m[b]!!.scream(m)
            return { a() / b() }
        }
    }
    class NumMonkey(a : String) : Monkey(a, "") {
        override fun scream(m: Map<String, Monkey>) :() -> Long = { a.toLong() }
    }

    class Human(var i : Long = 0L) : Monkey("", "") {
        override fun scream(m: Map<String, Monkey>) :() -> Long = { i }
    }
    class EqlMonkey(a: String, b: String) : Monkey(a, b) {
        override fun scream(m : Map<String, Monkey>) : () -> Long {
            val a = m[a]!!.scream(m)
            val b = m[b]!!.scream(m)
            return {
                (b().compareTo(a()).toLong())
            }
        }
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