package de.twittgen.aoc.current.y2021

import de.twittgen.aoc.Day
import kotlin.math.abs

class Day7 : Day<List<Int>>(){
    override fun String.parse() = split(",").map { it.toInt() }

    init {
        part1(37, 349769) { getFuelForAlignment(getMedian()) }
        part2(168, 99540554) {
            getMean().let{ listOf(getFuelForAlignment(it, gaussSum) , getFuelForAlignment(it+1, gaussSum)).minOrNull()!! }
        }
    }

    private fun List<Int>.getMean() = (sum().toDouble() / size).toInt()
    private fun List<Int>.getMedian() = sorted()[(size+1)/2]
    private fun List<Int>.getFuelForAlignment(i: Int, fuelFunc: (Int) -> Int = { it })  =
        sumOf { fuelFunc(abs(it - i)) }
    private val gaussSum : (Int) -> Int = { it*(it+1)/2 }

    override val example = """16,1,2,0,4,2,7,1,2,14"""
}

