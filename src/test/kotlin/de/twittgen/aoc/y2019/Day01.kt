package de.twittgen.aoc.y2019

import de.twittgen.aoc.Day

internal class Day01 : Day<Rocket>(){
    override fun String.parse() = filterNot { it == '\r' }.split("\n").map { it.toInt(10) }

    init {
        part1(null,3331849) { modules -> modules.sumOf { it.getFuelNeeded() } }
        part2(null,4994898) { modules -> modules.sumOf { it.getTotalFuel() } }
    }

    private fun Int.getFuelNeeded() =  this / 3 -2
    private fun Int.getTotalFuel(): Int = getFuelNeeded().let {  if(it <=0)  0 else it + it.getTotalFuel() }

}
private typealias Rocket = List<Int>
