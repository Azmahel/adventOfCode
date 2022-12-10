package de.twittgen.aoc.old.y2019

import de.twittgen.aoc.util.FileUtil
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

class Day9 {
    val input = FileUtil.readInput("2019/day9").split(",").map { it.toLong(10) }
    
    @Test
    fun getA() {
        runBlocking {
        val out = Channel<Long>(1000)
        val inch = Channel<Long>(199).also { ch ->
            println("sending in")
            launch {
                ch.send(1)
            }
        }
        val result = operateProgram(input, inch, out)
            out.close()
            val a = out.toList()
            val x =0
        }
    }

    @Test
    fun getB() {
        runBlocking {
            val out = Channel<Long>(1000)
            val inch = Channel<Long>(199).also { ch ->
                println("sending in")
                launch {
                    ch.send(2)
                }
            }
            val result = operateProgram(input, inch, out)
            out.close()
            val a = out.toList()
            val x =0
        }
    }

    @Test
    fun getAExamples() {
        runBlocking {
            val out = Channel<Long>(1000)
            val inch = Channel<Long>(199).also { ch ->
                println("sending in")
                launch {
                    ch.send(1)
                    ch.send(0)
                }
            }
            val result = operateProgram(listOf(109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99), inch, out)
           out.close()
            val a = out.toList()
            val x =0
        }
    }
    private suspend fun operateProgram(opcode: List<Long>, input: Channel<Long>, output: Channel<Long>): List<Long> {
        val r: MutableList<Long> = ArrayList<Long>(2000).also { list ->
            list.addAll(opcode)
            repeat(1000){ list.add(0)}}
        var i = 0L
        var rb =0L
        while (i <= r.size) {
            val operation = Operation.fromLong(r[i.toInt()])
            when (operation.opcode) {
                1L -> {
                    operation.p3Mode.assign(r,i+3,rb,performOperation(operation,i,r,rb) {x,y -> x+y})
                    i+=4
                }
                2L -> {
                    operation.p3Mode.assign(r,i+3,rb,performOperation(operation,i,r,rb) {x,y -> x*y})
                    i+=4
                }
                3L -> {
                    println("reading input")
                    operation.p1Mode.assign(r,i+1,rb,input.receive())
                    println("done reading input")
                    i+=2
                }
                4L -> {
                    val o = operation.p1Mode.getValue(r, i+1, rb)
                    println("publishing output $o")
                    output.send(o)
                    println("done publishing output")
                    i+=2
                }
                5L -> {
                    if(operation.p1Mode.getValue(r,i+1, rb)==0L) {
                        i +=3
                    } else {
                        i = operation.p2Mode.getValue(r,i+2, rb)
                    }
                }
                6L -> {
                    if(operation.p1Mode.getValue(r,i+1, rb)!=0L) {
                        i +=3
                    } else {
                        i = operation.p2Mode.getValue(r,i+2, rb)
                    }
                }
                7L ->{
                    operation.p3Mode.assign(r,i+3,rb, performOperation(operation,i,r, rb) {x,y -> if(x<y) 1 else 0} )
                    i +=4
                }
                8L ->{
                    operation.p3Mode.assign(r,i+3,rb, performOperation(operation,i,r, rb) {x,y -> if(x==y) 1 else 0})
                    i +=4
                }
                9L -> {
                    rb += operation.p1Mode.getValue(r,i+1L,rb)
                    i += 2
                }
                99L -> {
                    return r
                }
                else -> throw IllegalArgumentException("error in Programm at postition $i")
            }
        }
        return r
    }

    data class Operation(
        val opcode : Long,
        val p1Mode : Parametermode = Parametermode.IMMEDIATE,
        val p2Mode : Parametermode = Parametermode.IMMEDIATE,
        val p3Mode : Parametermode = Parametermode.IMMEDIATE
    ) {
        companion object {
            fun fromLong(i: Long) : Operation {
                val op = i.toString().padStart(5,'0')
                return Operation(
                    op.drop(3).toLong(10),
                    Parametermode.getByChar(op[2]),
                    Parametermode.getByChar(op[1]),
                    Parametermode.getByChar(op[0]).apply { }
                )
            }
        }
    }

    enum class Parametermode {
        POSTIONAL {
            override fun assign(list: MutableList<Long>, p: Long, rb: Long, value: Long) {
                list[list[p.toInt()].toInt()] = value
            }

            override fun getValue(list: MutableList<Long>, p: Long, rb: Long) = list[list[p.toInt()].toInt()]
        },
        IMMEDIATE {
            override fun assign(list: MutableList<Long>, p: Long, rb: Long, value: Long) {
                list[p.toInt()] = value
            }
            override fun getValue(list: MutableList<Long>, p: Long, rb: Long) = list[p.toInt()]
        },
        RELATIVE {
            override fun assign(list: MutableList<Long>, p: Long, rb: Long, value: Long) {
                list[(list[(p).toInt()]+rb).toInt()] = value
            }
            override fun getValue(list: MutableList<Long>, p: Long, rb: Long) = list[(list[(p).toInt()]+rb).toInt()]
        };
        companion object {
            fun getByChar(c: Char) = when (c ) {
                '0' -> POSTIONAL
                '1' -> IMMEDIATE
                '2' -> RELATIVE
                else -> throw IllegalArgumentException()
            }

        }
        abstract fun getValue (list: MutableList<Long>, p: Long, rb:Long) : Long
        abstract fun assign(list: MutableList<Long>, p: Long, rb:Long, value:Long)
    }
    fun performOperation(operation: Operation, index: Long, list: MutableList<Long>, rb : Long, block: (Long, Long)->Long) : Long {
        return block(operation.p1Mode.getValue(list, index + 1, rb), operation.p2Mode.getValue(list, index + 2, rb))
    }
}