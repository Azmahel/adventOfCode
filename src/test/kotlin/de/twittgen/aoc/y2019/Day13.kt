package de.twittgen.aoc.y2019

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import org.junit.jupiter.api.Test
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.sign

class Day13 {
    val input = this::class.java.getResource("day13").readText().split(",").map { it.toLong(10) }

    @InternalCoroutinesApi
    @ExperimentalCoroutinesApi
    @Test
    fun getA() {
        runBlocking {
            val cToG = Channel<Long>(100)
            val program = async { operateProgram(input,Channel(), cToG) }
            val game = async { operateGame(cToG) }
            program.await()
            val map =game.await()
            println(map.values.count { it ==2L })
        }
    }

    @InternalCoroutinesApi
    @ExperimentalCoroutinesApi
    @Test
    fun getB() {
        runBlocking {

                    map.keys.forEach { map.remove(it)  }
                    val pToG = Channel<Long>(1)
                    val cToP = Channel<Long>(1)
                    launch(Dispatchers.IO) {operateJoystick(cToP)}
                    val program = async { operateProgram(listOf(2L) + input.drop(1),cToP, pToG) }
                    val game = async { operateGame(pToG) }
                    program.await()
                    game.await()
                    //printMap(map)
        }
        println(map[-1L to 0L])
    }

    @ExperimentalCoroutinesApi
    private suspend fun operateJoystick(output:Channel<Long>) {
            //printMap(map)
        while(true){

                val ball = map.entries.filter { it.value == 4L }.map{it.key}.firstOrNull()?.first ?:0
                val paddle = map.entries.filter { it.value == 3L }.map{it.key}.firstOrNull()?.first ?:0



                if(!output.isClosedForSend)output.send((ball-paddle).sign.toLong())
                printMap(map)
                delay(10)
            }

    }

    val map = ConcurrentHashMap<Pair<Long,Long>,Long>()

    @InternalCoroutinesApi
    @ExperimentalCoroutinesApi
    private suspend fun operateGame(input:Channel<Long>): Map<Pair<Long,Long>,Long> {

        while(!input.isClosedForReceive) {
            val x = input.receiveOrClosed().valueOrNull ?:0
            val y = input.receiveOrClosed().valueOrNull ?:0
            val z = input.receiveOrClosed().valueOrNull ?:0
            map[x to y] = z
            //if(x <0 ) printMap(map)
        }
        return map.toMap()
    }

    private fun printMap(map: Map<Pair<Long, Long>, Long>) {
        if ( map.isEmpty()) return
        val maxX = map.keys.map{it.first}.maxOrNull()!!
        val maxY = map.keys.map{it.second}.maxOrNull()!!
        println("Score: ${map[-1L to 0L]}")
        println(
            (0..maxY).joinToString("\n") { y ->
                (0..maxX).joinToString("") { x ->
                    when (map[x to y]) {
                        1L -> "|"
                        2L -> "o"
                        3L -> "_"
                        4L -> "B"
                        else -> " "
                    }
                }
            }
        )
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
                    operation.p1Mode.assign(r,i+1,rb,input.receive())
                    i+=2
                }
                4L -> {
                    val o = operation.p1Mode.getValue(r, i+1, rb)
                    output.send(o)
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
                    output.close()
                    return r
                }
                else -> throw IllegalArgumentException("error in Programm at postition $i")
            }
        }
        output.close()
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