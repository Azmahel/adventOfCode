package de.twittgen.aoc.current.y2020

import de.twittgen.aoc.current.Day

class Day13 : Day<Pair<Int, List<String>>>() {
    override fun String.parse() = lines().let { (now, busses) -> now.toInt() to busses.split(',') }

    init {
        part1(295, 171) { (now, busses ) ->
            busses.getNextArrival(now).let { (id, waitTime) -> id.toInt() * waitTime }
        }
        part2(1068781, 539746751134958) { (_, busses) ->
            val modulo = busses.mapIndexedNotNull { i, it -> if (it == "x") null else it.toInt() to it.toInt()-i }
            chineseRemainder(modulo).let { (a,b) -> a mod b }
        }
    }

    private fun List<String>.getNextArrival(now: Int) = filterNot { it == "x" }
        .map { it to (it.toInt() - (now % it.toInt())) }
        .minByOrNull { it.second }!!

    private fun chineseRemainder(modulo: List<Pair<Int,Int>>): Pair<Long, Long> {
        val M = modulo.fold(1L) { x, y -> x* y.first}
        val aes = modulo.map { (mi, ai) ->
            val Mi = M/mi
            val (_,_,si) = extendedEuclidean(mi.toLong(), Mi)
            val ei = si*Mi
            ai.toLong()*ei
        }
        return aes.sum() to M
    }

    private infix fun Long.mod(other: Long): Long = Math.floorMod(this,other)

    private fun extendedEuclidean(a: Long, b: Long): Triple<Long, Long, Long> {
        if(b == 0L)  return Triple(a,1, 0)
        val ( d1 , s1, t1 ) = extendedEuclidean(b, a mod b)
        return Triple(d1, t1, s1-(a/b)*t1)
    }

    override val example = """
        939
        7,13,x,x,59,x,31,19
    """.trimIndent()
}