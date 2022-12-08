package de.twittgen.aoc.y2019

import de.twittgen.aoc.util.FileUtil
import org.junit.jupiter.api.Test
import kotlin.math.absoluteValue

class Day16 {
    val input = FileUtil.readInput("2019/day16").toIntList()

    val basePattern = listOf(0,1,0,-1)
    private fun getPatterns(length: Int) =
        (1..length).map { n ->
            val pattern = basePattern.flatMap { point -> listOf(point).repeated(n) }
            pattern.repeated((length/pattern.size)+1).drop(1).take(length)
        }

    @Test
    fun getA() {
        var list = input
        list = ffT(list,100 )
        println(list.take(8).joinToString(""))
    }

    @Test
    fun getB() {
        val offset = input.take(7).joinToString("").toInt()
        val list = input.repeated(10000)
        require(list.size < 2*offset)
        var result = list.drop(offset)
        repeat(100) {
            var sum = 0
            result = result.reversed().map{
                sum += it
                (sum % 10).absoluteValue
            }.reversed()
            val x =0
        }
        println(result.take(8).joinToString(""))
    }
    private fun ffT(
        list: List<Int>, times: Int
    ): List<Int> {
        val patterns = getPatterns(list.size)
        var result = list
        repeat(times) {
            result = (1..result.size).map { result }.zip(patterns).map {
                it.first.applyPattern(it.second)
            }
        }
        return result
    }
}

private fun  List<Int>.applyPattern(pattern: List<Int>) = (zip(pattern).map { it.first * it.second }.sum() % 10).absoluteValue

private fun String.toIntList() : List<Int> = map{it.toString().toInt() }

private fun <E> List<E>.repeated(times: Int) = (1..times).flatMap{this}
