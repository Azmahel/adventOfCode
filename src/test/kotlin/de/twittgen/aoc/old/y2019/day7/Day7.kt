package de.twittgen.aoc.old.y2019.day7

import de.twittgen.aoc.util.FileUtil.readInput
import de.twittgen.aoc.util.toIntcodeProgram
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

class Day7 {

    val input = readInput("2019/day7").toIntcodeProgram()

    @Test
    fun getA() {
        val x = (5..9).flatMap {a ->
            (5..9).filter { it != a }.flatMap { b->
                (5..9).filter { it != a && it !=b}.flatMap { c->
                    (5..9).filter { it != a  && it != b && it != c }.flatMap { d ->
                        (5..9).filter { it != a  && it != b && it != c && it != d }.flatMap { e ->
                            runBlocking {
                                val aToB = Channel<Int>(5).also { launch { it.send(b) } }
                                val bToC = Channel<Int>(5).also { launch { it.send(c) } }
                                val cToD = Channel<Int>(5).also { launch {it.send(d) } }
                                val dToE = Channel<Int>(5).also { launch {it.send(e) } }
                                val eToA = Channel<Int>(5).also {
                                    launch {
                                        it.send(a)
                                        it.send(0)
                                    }
                                }
                                val amps = listOf(
                                    async { operateProgram(input.toMutableList(), eToA, aToB) },
                                    async { operateProgram(input.toMutableList(), aToB, bToC) },
                                    async { operateProgram(input.toMutableList(), bToC, cToD) },
                                    async { operateProgram(input.toMutableList(), cToD, dToE) },
                                    async { operateProgram(input.toMutableList(), dToE, eToA) }
                                )
                                amps.forEach { it.await() }
                                listOf(eToA.receive())
                            }

                        }
                    }
                }
            }
        }.maxOrNull()
        println(x)
    }
    private suspend fun operateProgram(opcode: List<Int>, input: Channel<Int>, output: Channel<Int>): List<Int> {
        var r =opcode.toMutableList()
        var i = 0
        while (i <= r.size) {
            val operation = Operation.fromInt(r[i])
            when (operation.opcode) {
                1 -> {
                    r[r[i+3]] = performOperation(operation,i,r) {x,y -> x+y}
                    i+=4
                }
                2 -> {
                    r[r[i+3]] = performOperation(operation,i,r) {x,y -> x*y}
                    i+=4
                }
                3 -> {
                    r[r[i+1]]=input.receive()
                    i+=2
                }
                4 -> {
                    output.send(operation.p1Mode.getValue(r, i+1))
                    i+=2
                }
                5 -> {
                    if(operation.p1Mode.getValue(r,i+1)==0) {
                        i +=3
                    } else {
                        i = operation.p2Mode.getValue(r,i+2)
                    }
                }
                6 -> {
                    if(operation.p1Mode.getValue(r,i+1)!=0) {
                        i +=3
                    } else {
                        i = operation.p2Mode.getValue(r,i+2)
                    }
                }
                7 ->{
                    r[r[i+3]] = performOperation(operation,i,r) {x,y -> if(x<y) 1 else 0}
                    i +=4
                }
                8 ->{
                    r[r[i+3]] = performOperation(operation,i,r) {x,y -> if(x==y) 1 else 0}
                    i +=4
                }
                99 -> return r
                else -> throw IllegalArgumentException("error in Programm at postition $i")
            }
        }
        return r
    }

    data class Operation(
        val opcode : Int,
        val p1Mode : Parametermode = Parametermode.IMMEDIATE,
        val p2Mode : Parametermode = Parametermode.IMMEDIATE,
        val p3Mode : Parametermode = Parametermode.IMMEDIATE
    ) {
        companion object {
            fun fromInt(i: Int) : Operation {
                val op = i.toString().padStart(5,'0')
                return Operation(
                    op.drop(3).toInt(10),
                    Parametermode.getByChar(op[2]),
                    Parametermode.getByChar(op[1]),
                    Parametermode.getByChar(op[0]).apply { }
                )
            }
        }
    }

    enum class Parametermode {
        POSITIONAL {
            override fun getValue(list: MutableList<Int>, p: Int) = list[list[p]]
        },
        IMMEDIATE {
            override fun getValue(list: MutableList<Int>, p: Int) = list[p]

        };
        companion object {
            fun getByChar(c: Char) = if(c =='0') POSITIONAL else IMMEDIATE
        }
        abstract fun getValue (list: MutableList<Int>, p: Int) : Int
    }
    fun performOperation(operation: Operation, index: Int, list: MutableList<Int>, block: (Int, Int)->Int) : Int {
        return block(operation.p1Mode.getValue(list, index + 1), operation.p2Mode.getValue(list, index + 2))
    }
}