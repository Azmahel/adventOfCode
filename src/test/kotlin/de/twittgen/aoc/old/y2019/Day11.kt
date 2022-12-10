package de.twittgen.aoc.old.y2019

import de.twittgen.aoc.util.FileUtil
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

class Day11 {

    val input =  FileUtil.readInput("2019/day11").split(",").map { it.toLong(10) }

    @ExperimentalCoroutinesApi
    @Test
    fun getA() {
        runBlocking{
            val rToC = Channel<Long>(5)
            val cToR = Channel<Long>(5)
            val robot = async { operateRobot(cToR, rToC, false)}
            val computer = async { operateProgram(input,rToC,cToR)}
            val resultC = computer.await()
            val (white, painted) = robot.await()
            val x =0
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getB() {
        runBlocking{
            val rToC = Channel<Long>(5)
            val cToR = Channel<Long>(5)
            val robot = async { operateRobot(cToR, rToC, true)}
            val computer = async { operateProgram(input,rToC,cToR)}
            val resultC = computer.await()
            val (white, painted) = robot.await()
            val minX = white.map{it.first}.minOrNull()!!
            val maxX = white.map{it.first}.maxOrNull()!!
            val minY = white.map{it.second}.minOrNull()!!
            val maxY = white.map{it.second}.maxOrNull()!!
            (minY..maxY).forEach {y ->
                (minX..maxX).forEach {x ->
                    if(white.contains(x to y)) print("#") else print(" ")
                }
                println()
            }
        }
        val x =0
    }

    @ExperimentalCoroutinesApi
    private suspend fun  operateRobot(input: Channel<Long>, output: Channel<Long>, startOnWhite: Boolean): Pair<List<Pair<Int, Int>>, List<Pair<Int, Int>>> {
        val robot = Robot()
        var turnMode = false
        val whitePanels = mutableListOf<Pair<Int,Int>>().also { if(startOnWhite) it.add(robot.position)}
        val painted = mutableSetOf<Pair<Int,Int>>()
        output.send( if(whitePanels.contains(robot.position)) 1 else 0 )
        while(!input.isClosedForReceive) {
            val i = input.receive()
            when(turnMode) {
                true -> {
                    robot.facing = robot.facing.turn(i)
                    robot.position = robot.facing.moveFrom(robot.position)

                    output.send(if (whitePanels.contains(robot.position)) 1 else 0)
                }
                false ->{
                    when(i){
                        1L -> { whitePanels.add(robot.position)}
                        0L -> { whitePanels.remove(robot.position)}
                        else -> {}
                    }
                    painted.add(robot.position)
                }
            }
            turnMode = !turnMode
        }
        return whitePanels.toList() to painted.toList()
    }

    class Robot() {
        var position = 0 to 0
        var facing = Facing.UP

        enum class Facing {
             UP {
                 override fun moveFrom(p: Pair<Int, Int>) = p.first to p.second -1
                 override fun turn(code: Long): Facing = if (code == 1L) RIGHT else LEFT
             },
            DOWN {
                override fun moveFrom(p: Pair<Int, Int>) = p.first to p.second +1
                override fun turn(code: Long): Facing = if(code == 1L) LEFT else RIGHT
            },
            LEFT {
                override fun moveFrom(p: Pair<Int, Int>) = p.first-1 to p.second
                override fun turn(code: Long): Facing = if(code == 1L) UP else DOWN
            },
            RIGHT {
                override fun moveFrom(p: Pair<Int, Int>) = p.first+1 to p.second
                override fun turn(code: Long): Facing = if(code == 1L) DOWN else UP
            };

            abstract fun turn(code : Long): Facing
            abstract fun moveFrom(p: Pair<Int,Int>): Pair<Int,Int>
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