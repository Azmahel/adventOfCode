package de.twittgen.aoc.y2020

import de.twittgen.aoc.y2019.shared.util.FileUtil
import de.twittgen.aoc.y2019.shared.util.second
import org.junit.jupiter.api.Test

class day13 {
    val input = FileUtil.readInput("2020/day13")
    val example = """
        939
        7,13,x,x,59,x,31,19
    """.trimIndent()


    fun parseInput(s: String): Pair<Int, List<String>> {
        val now = s.lines().first().toInt()
        val busses = s.lines().second().split(',')
        return now to busses
    }

    @Test
    fun example() {
        val (now, busses) = parseInput(example)
        val nextArrivals = busses
            .filterNot { it== "x" }
            .map {
                it to (it.toInt() -(now % it.toInt()))
            }
        val nextBus = nextArrivals.minBy { it.second }
        assert(
            nextBus!!.first == "59" && nextBus.second == 5
        )
    }

    @Test
    fun part1() {
        val (now, busses) = parseInput(input)
        val nextArrivals = busses
            .filterNot { it== "x" }
            .map {
                it to (it.toInt() -(now % it.toInt()))
            }
        val nextBus = nextArrivals.minBy { it.second }
        println(
            nextBus!!.first.toInt() * nextBus.second
        )
    }

    @Test
    fun part2() {
        val (_,busses) = parseInput(input)
        val modulos = busses.mapIndexedNotNull { i ,it ->
            if (it == "x") null else it.toInt() to it.toInt()-i
        }
        val result = chineseRemainder(modulos)
        val x = (result.first mod result.second)
        println(x)
    }

    fun chineseRemainder(modulos: List<Pair<Int,Int>>): Pair<Long, Long> {
        val M = modulos.fold(1L) {x,y -> x* y.first}
        val aes = modulos.map { (mi, ai) ->
            val Mi = M/mi
            val (_,_,si) = extendedEuklidean(mi.toLong(), M/mi)
            val ei = si*Mi
            ai.toLong()*ei
        }
        return aes.sum() to M
    }

     infix fun Long.mod(other: Long): Long = Math.floorMod(this,other)

    private fun extendedEuklidean(a: Long, b: Long): Triple<Long, Long, Long> {
        if(b == 0L)  return Triple(a,1, 0)
        val ( d1 , s1, t1 ) = extendedEuklidean(b, a mod b)
        return Triple(d1, t1, s1-(a/b)*t1)
    }
}