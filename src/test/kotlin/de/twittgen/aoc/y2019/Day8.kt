package de.twittgen.aoc.y2019

import de.twittgen.aoc.util.FileUtil
import org.junit.jupiter.api.Test

class Day8 {
    val input = FileUtil.readInput("2019/day8").chunked(25 * 6)
    @Test
    fun getA() {
        val x = input.minByOrNull {
            it.count { it=='0' }
        }!!.let {
            it.count{ it == '1' } * it.count{ it == '2' }
        }
        println(x)
    }

    @ExperimentalStdlibApi
    @Test
    fun getB(){
        var image = input.last().toCharArray()
        input.reversed().forEach {
            image = it.mapIndexed {i, c -> if(c != '2') c else image[i] }.toCharArray()
        }
        val result = image.concatToString().replace("0"," ").replace("1","X")
        result.chunked(25).forEach { println(it) }
    }

}