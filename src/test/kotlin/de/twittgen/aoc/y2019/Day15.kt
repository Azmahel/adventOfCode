package de.twittgen.aoc.y2019

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import org.junit.jupiter.api.Test
import kotlin.math.absoluteValue

class Day15 {
    val input = this::class.java.getResource("day15").readText().split(",").map { it.toLong(10) }

    @Test
    fun getA() {
        val map = exploreMap()
        printMap(map, 0 to 0)
        val p = getShortestPath(map)
        println(p.size -1)
    }

    @Test
    fun getB() {
        val map = exploreMap()
        val duration = fillMap(map)
        println(duration)
    }

    private fun fillMap(map: Map<Pair<Int, Int>, Tile>): Int {
        val start = map.filter{ it.value == Tile.OXYGEN }.keys.first()
        val usableTiles = map.filter { it.value != Tile.WALL }.keys.toList()
        var oxygenated = setOf(start)
        var duration =0
        while(oxygenated.size < usableTiles.size) {
            duration ++
            oxygenated = oxygenated + oxygenated.flatMap { it.getAdjacent(usableTiles) }.toSet()
        }
        return duration
    }
    private fun exploreMap(): Map<Pair<Int,Int>, Tile> {
        return runBlocking {
            val rtoC = Channel<Long>()
            val ctoR = Channel<Long>()
            val robot = async { controlRobot(ctoR,rtoC) }
            val program = launch { operateProgram(input,rtoC,ctoR) }
            val map = robot.await()
            program.cancelAndJoin()
            map
        }
    }

    private fun getShortestPath(map: Map<Pair<Int, Int>, Tile>): List<Pair<Int,Int>> {
        val usableTiles = map.filter { it.value != Tile.WALL }.keys.toList()
        val start = map.filter { it.value == Tile.START }.keys.first()
        var paths = listOf(listOf(start))
        while(true) {
            paths = paths.flatMap { path ->
                path.last().getAdjacent(usableTiles).mapNotNull { if(path.contains(it)) null else path + it }
            }.distinctBy { it.last() }
            paths.firstOrNull { map[it.last()] == Tile.OXYGEN }?.let {
                return it
            }
        }
    }

    private fun Pair<Int,Int>.getAdjacent(map: List<Pair<Int,Int>>): List<Pair<Int,Int>> {
        return map.filter { (it.first - first).absoluteValue + (it.second - second).absoluteValue == 1}
    }

    private suspend fun controlRobot(input: Channel<Long>, output: Channel<Long>): Map<Pair<Int, Int>, Tile> {
        val start = 0 to 0
        val robot = Robot()
        val map = mutableMapOf((0 to 0) to Tile.START)
        var oxygenFound = false
        while(!oxygenFound || (oxygenFound && robot.position != start)) {
            output.send(robot.facing.code)
            when(input.receive()) {
                0L -> {
                    map[robot.facing.moveFrom(robot.position)] =
                        Tile.WALL
                    robot.facing = robot.facing.turnOnWall()
                }
                1L -> {
                    robot.position = robot.facing.moveFrom(robot.position)
                    map[robot.position] = Tile.EMPTY
                    robot.facing = robot.facing.turn()
                }
                2L -> {
                    robot.position = robot.facing.moveFrom(robot.position)
                    map[robot.position] = Tile.OXYGEN
                    robot.facing = robot.facing.turn()
                    oxygenFound = true
                }
            }
            //printMap(map, robot.position)
        }
        map[start] = Tile.START
        return map.toMap()
    }

    private fun printMap(map: Map<Pair<Int, Int>, Tile>, position: Pair<Int,Int>) {
        val mapWithRobot = map.filter{ it.key != position}.plus(position to Tile.ROBOT)
        if (mapWithRobot.isEmpty()) return
        val maxX = mapWithRobot.keys.map{it.first}.maxOrNull()!!
        val minX = mapWithRobot.keys.map{it.first}.minOrNull()!!
        val maxY = mapWithRobot.keys.map{it.second}.maxOrNull()!!
        val minY = mapWithRobot.keys.map{it.second}.minOrNull()!!
        println(
            (minY..maxY).joinToString("\n") { y ->
                (minX..maxX).joinToString("") { x ->
                    (mapWithRobot[x to y] ?: Tile.UNKNOWN).value
                }
            }
        )
    }

    enum class Tile(val value: String) {
        EMPTY("."),
        ROBOT("R"),
        WALL("|"),
        OXYGEN("O"),
        UNKNOWN("?"),
        START("s")
    }
    class Robot {
        var position = 0 to 0
        var facing = Facing.UP

        enum class Facing {
            UP {
                override val code = 1L
                override fun moveFrom(p: Pair<Int, Int>) = p.first to p.second -1
                override fun turn(): Facing =
                    RIGHT
                override fun turnOnWall(): Facing =
                    LEFT

            },
            DOWN {
                override val code = 2L
                override fun moveFrom(p: Pair<Int, Int>) = p.first to p.second +1
                override fun turn(): Facing =
                    LEFT
                override fun turnOnWall(): Facing =
                    RIGHT

            },
            LEFT {
                override val code = 3L
                override fun moveFrom(p: Pair<Int, Int>) = p.first-1 to p.second
                override fun turn(): Facing =
                    UP
                override fun turnOnWall(): Facing =
                    DOWN
            },
            RIGHT {
                override val code = 4L
                override fun moveFrom(p: Pair<Int, Int>) = p.first+1 to p.second
                override fun turn(): Facing =
                    DOWN
                override fun turnOnWall(): Facing =
                    UP
            };
            abstract val code: Long
            abstract fun turn(): Facing
            abstract fun turnOnWall(): Facing
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
    private fun performOperation(operation: Operation, index: Long, list: MutableList<Long>, rb : Long, block: (Long, Long)->Long) : Long {
        return block(operation.p1Mode.getValue(list, index + 1, rb), operation.p2Mode.getValue(list, index + 2, rb))
    }
}