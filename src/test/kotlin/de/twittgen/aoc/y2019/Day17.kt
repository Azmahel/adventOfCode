package de.twittgen.aoc.y2019

import de.twittgen.aoc.y2019.shared.Point2D
import de.twittgen.aoc.y2019.shared.util.FileUtil
import de.twittgen.aoc.y2019.shared.util.toIntcodeProgram
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import java.lang.NumberFormatException

class Day17 {
    val input = FileUtil.readInput("day17").toIntcodeProgram().map{ it.toLong()}

    @Test
    fun getA() {
        val map = runBlocking {
            val out = Channel<Long>(10000)

            val program = async { operateProgram(input, output = out) }

            program.await()
            out.toList().map { it.toChar() }
        }
            println(map.joinToString(""))
            val intersections = findIntersections(map.toMap())
            val ap = intersections.map{ (x,y) -> x*y }
            println(ap.sum())

    }

    @Test
    fun getB() {
        runBlocking {
            val out = Channel<Long>(10000)
            val program = async { operateProgram(input, output = out) }
            program.await()
            val fields = out.toList().map { it.toChar() }
            println(fields.joinToString(""))
            val map = fields.toMap()
            val robot = map.run {
                when {
                    containsValue('^') -> Robot(
                        keys.first { get(it) == '^' },
                        Facing.UP
                    )
                    containsValue('<') -> Robot(
                        keys.first { get(it) == '<' },
                        Facing.LEFT
                    )
                    containsValue('>') -> Robot(
                        keys.first { get(it) == '>' },
                        Facing.RIGHT
                    )
                    containsValue('v') -> Robot(
                        keys.first { get(it) == 'v' },
                        Facing.DOWN
                    )
                    else -> error("no Robot found")
                }
            }
            val path = robot.findPath(map.toScaffolding())
            val compressed = path.compress()

            val out2 = Channel<Long>(100000)
            val send = Channel<Long>(1)
            launch {
                compressed.toInputString().forEach { send.send(it.toLong()) }
                send.send('\n'.toLong())
                send.send('n'.toLong())

                send.send('\n'.toLong())
            }
            val program2 = async { operateProgram(listOf(2L) + input.drop(1), send, out2) }
            program2.await()
            val dust = out2.toList()
            println(path)
        }
    }

    private fun List<String>.compress(): CompressResult {
        val a = getSubPath()
        var remainder = joinToString(", ").replace(a.joinToString(", "), "A").split(", ")
        val b = remainder.getSubPath()
        remainder = remainder.joinToString(", ").replace(b.joinToString(", "), "B").split(", ")
        val c = remainder.getSubPath().toMutableList()
        var it2 = remainder.joinToString(", ").replace(c.joinToString(", "), "C").split(", ")
        while(it2.any{ !listOf("A","B","C").contains(it)}) {
            c.addAll( it2.getSubPath().filter{ it != "C"})
            it2 = remainder.joinToString(", ").replace(c.joinToString(", "), "C").split(", ")
        }
        remainder = remainder.joinToString(", ").replace(c.joinToString(", "), "C").split(", ")
        return CompressResult(remainder, a, b, c)
    }

    private data class CompressResult(
        val main : List<String>,
        val a: List<String>,
        val b: List<String>,
        val c: List<String>
    ) {
        fun toInputString() = listOf(
            main.joinToString(","),
            a.joinToString(","),
            b.joinToString(","),
            c.joinToString(",")
        ).joinToString("\n")
    }
    private fun List<String>.toAscii() = map { if(it.isNumber()) it.toInt() else it[1].toInt() }

    private fun String.isNumber(): Boolean {
        return try {
            toInt()
            true
        }catch( e: NumberFormatException){ false }
    }
    private fun List<String>.getSubPath() : List<String> {
        val a =  mutableListOf<String>()
        var it = dropWhile { it == "A" || it == "B"  }

        a += it.take(1)
        var i=0
        while(it.drop(1).joinToString(" ,").contains(it.take(2).joinToString(" ,"))
            && !it[1].contains("x")
            && !it[1].contains("A")
            && !it[1].contains("B")
        ) {
            a += it. drop(1).first()
            it = it.joinToString(", ").replace(it.take(2).joinToString(", "), "x[${i++}]").split(", ")
        }
        return a
    }
    val A = listOf( "R", "8", "L", "10", "L", "12", "R", "4")
    val B = listOf( "R", "8", "L", "12", "R", "4", "R", "4")
    val C = listOf( "R", "8", "L", "10", "R", "8")
    private fun findIntersections(input: Map<Point2D, Char>): List<Point2D> {
        val scaffolding = input.toScaffolding()
       return scaffolding.keys.filter{ (x,y) ->
            scaffolding.containsKey(Point2D(x+1 , y))
                    && scaffolding.containsKey(Point2D(x-1 , y))
                    && scaffolding.containsKey(Point2D(x , y+1))
                    && scaffolding.containsKey(Point2D(x , y-1))
        }

    }

    private fun Map<Point2D, Char>.toScaffolding() = filterValues { it == '#' }

    private fun List<Char>.toMap(): Map<Point2D, Char> {
        val columns = indexOfFirst { it == '\n' }
        return filter { it != '\n' }.mapIndexed { i, it -> Point2D(i % columns ,i / columns) to it }.toMap()
    }

    private data  class Robot(var position: Point2D, var facing: Facing) {
        fun findPath(map : Map<Point2D, Char>) : List<String> {
            val path = mutableListOf<String>()
            var currentCount = 0
            while(true) {
                when {
                    map.contains(facing.getAhead(position)) -> {
                        currentCount ++
                        position = facing.getAhead(position)
                    }
                    map.contains(facing.right.getAhead(position)) -> {
                        if (currentCount != 0) path.add("$currentCount")
                        currentCount =0
                        path.add(Facing.RIGHT.code)
                        facing = facing.right
                    }
                    map.contains(facing.left.getAhead(position)) -> {
                        if (currentCount != 0) path.add("$currentCount")
                        currentCount =0
                        path.add(Facing.LEFT.code)
                        facing = facing.left
                    }
                    else -> {
                        path.add("$currentCount")
                        return path
                    }
                }
            }
        }
    }
    private data class Command(val facing: Facing, val distance: Int )
    private enum class Facing(val code: String) {
        UP("U") {
            override val left: Facing get() = LEFT
            override val right: Facing get()= RIGHT
            override fun getAhead(it: Point2D): Point2D =   it.run{ Point2D(x, y -1)}

        },
        DOWN("D")  {
            override val left: Facing get() = RIGHT
            override val right: Facing get()= LEFT
            override fun getAhead(it: Point2D): Point2D =  it.run{ Point2D(x, y +1)}

        },
        LEFT("L") {
            override val left: Facing get() = DOWN
            override val right: Facing get()= UP
            override fun getAhead(it: Point2D): Point2D =  it.run{ Point2D(x-1, y )}

        },
        RIGHT("R") {
            override val left: Facing get() = UP
            override val right: Facing get()= DOWN
            override fun getAhead(it: Point2D): Point2D =  it.run{ Point2D(x+1, y )}
        };

        abstract val left  : Facing
        abstract val right : Facing
        abstract fun getAhead(it: Point2D) : Point2D
    }
    private suspend fun operateProgram(opcode: List<Long>, input: Channel<Long> = Channel(), output: Channel<Long> = Channel()): List<Long> {
        val r: MutableList<Long> = ArrayList<Long>(80000).also { list ->
            list.addAll(opcode)
            repeat(10000){ list.add(0)}}
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