package de.twittgen.aoc.y2019

import org.junit.jupiter.api.Test
import kotlin.math.absoluteValue

class Day10 {
    private val input = this::class.java.getResource("day10").readText().split("\r\n").toCoordinates()

    @Test
    fun getA() {
        val result = input
            .map { input.toVectorsFrom(it) }
            .map {
                //get all Directions in which there is at least 1 Asteroid
                it.filterNot{ it == 0 to 0 }
                    .map{ it.normalize() }
                    .distinct()
            }
            //get the largest number of visible Asteroids
            .map { it.size }.maxOrNull()
        println(result)
    }
    @Test
    fun getB() {
        //transform to list of targeting vectors to closest Asteroids
        val targetingMaps = input.map { input.toVectorsFrom(it) }.map { it.filterNot{ it == 0 to 0 }.sortedBy { it.length() }.distinctBy {it.normalize()} }
        //get longest List of targeting vectors
        val targetingVectors =  targetingMaps.maxByOrNull{ it.size}!!
        //get our base position
        val base = input[targetingMaps.indexOf(targetingVectors)]
        val targets =(targetingVectors.sortByAngleFromVertical()).map { it.first + base.first to it.second + base.second}
        val result = targets[199]
        println(result)
    }

    private fun List<Pair<Int, Int>>.sortByAngleFromVertical(): List<Pair<Int, Int>> {
        val q1 = filter { it.first >=0  && it.second <0 }.sortedBy{ it.normalize().first }
        val q2 = filter { it.first >=0  && it.second >=0 }.sortedByDescending{ it.normalize().first }
        val q3 = filter { it.first <0  && it.second >0 }.sortedByDescending{ it.normalize().first }
        val q4 = filter { it.first <0  && it.second <=0 }.sortedBy{ it.normalize().first }
        return q1 + q2 + q3 + q4
    }

    private fun List<String>.toCoordinates(): List<Pair<Int, Int>> {
       return  mapIndexed{ y , r ->
            r.mapIndexedNotNull { x, c ->
                if (c=='#') x to y else null
            }
        }.flatten()
    }

    private fun List<Pair<Int,Int>>.toVectorsFrom(por: Pair<Int,Int>): List<Pair<Int,Int>> = map{ toVector(por, it) }
    private fun toVector(it: Pair<Int, Int>, p1: Pair<Int, Int>) = p1.first - it.first to p1.second - it.second
    private fun Pair<Int,Int>.normalize() : Pair<Double, Double> = first / length() to second / length()
    private fun Pair<Int, Int>.length() = (first.absoluteValue + second.absoluteValue).toDouble()

}