package de.twittgen.aoc.old.y2019.day5

import de.twittgen.aoc.util.toInt

private const val ADD = 1
private const val MULT = 2
private const val READ_IN = 3
private const val WRITE_OUT = 4
private const val JUMP_IF_TRUE = 5
private const val JUMP_IF_FALSE = 6
private const val LESS_THAN = 7
private const val EQUALS = 8
private const val STOP = 99
class Day5IntCodeComputer(private val program: List<Int>) {

    fun run(input: Int): List<Int> {
        val output = mutableListOf<Int>()
        var r =program.toMutableList()
        var i = 0
        while (i <= r.size) {
            val operation = Operation.fromInt(r[i])
            when (operation.opcode) {
                ADD -> {
                    r[r[i+3]] = performOperation(operation,i,r) {x,y -> x+y}
                    i+=4
                }
                MULT -> {
                    r[r[i+3]] = performOperation(operation,i,r) {x,y -> x*y}
                    i+=4
                }
                READ_IN -> {
                    r[r[i+1]]=input
                    i+=2
                }
                WRITE_OUT -> {
                    output.add(operation.p1Mode.getValue(r, i+1))
                    i+=2
                }
                JUMP_IF_TRUE -> {
                    if(operation.p1Mode.getValue(r,i+1)==0) {
                        i +=3
                    } else {
                        i = operation.p2Mode.getValue(r,i+2)
                    }
                }
                JUMP_IF_FALSE -> {
                    if(operation.p1Mode.getValue(r,i+1)!=0) {
                        i +=3
                    } else {
                        i = operation.p2Mode.getValue(r,i+2)
                    }
                }
                LESS_THAN ->{
                    r[r[i+3]] = performOperation(operation,i,r) {x,y -> (x<y).toInt()}
                    i +=4
                }
                EQUALS ->{
                    r[r[i+3]] = performOperation(operation,i,r) {x,y -> (x==y).toInt()}
                    i +=4
                }
                STOP -> return output
                else -> throw IllegalArgumentException("error in Programm at postition $i")
            }
        }
        return output
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
    private fun performOperation(operation: Operation, index: Int, list: MutableList<Int>, block: (Int, Int)->Int) : Int {
        return block(operation.p1Mode.getValue(list, index + 1), operation.p2Mode.getValue(list, index + 2))
    }
}