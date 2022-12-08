package de.twittgen.aoc.y2019

import de.twittgen.aoc.util.Point2D
import de.twittgen.aoc.util.FileUtil.readInput

class Day18 {
 val input = readInput("2019/day18")
     .replace("\r","")
     .split("\n")
     .mapIndexed { y, it ->
         it.mapIndexed{ x, char ->
             Point2D(x,y) to char
         }
     }.flatten()
     .filterNot{ (_,char) -> char =='#'}
     .toMap()

    fun getA() {
        val keyPositions = input.filter { (_,it) -> ('a'..'z').contains(it) }
        val entrancePosition = input.filterValues { it == '@' }.entries.first()

    }

    private fun getPathToKeys(start: Point2D, keys: Map<Point2D,Char>, map: Map<Point2D,Char> ) {


    }

/*    fun Point2D.getAdjacent()=  return map.filter { (it.first - first).absoluteValue + (it.second - second).absoluteValue == 1}

    private fun getShortestPath(map: Map<Pair<Int, Int>, Tile>): List<Pair<Int,Int>> {
        val usableTiles = map.filter { it.value != Tile.WALL }.keys.toList()
        val start = map.filter { it.value == Tile.START}.keys.first()
        var paths = listOf(listOf(start))
        while(true) {
            paths = paths.flatMap { path ->
                path.last().getAdjacent(usableTiles).mapNotNull { if(path.contains(it)) null else path + it }
            }.distinctBy { it.last() }
            paths.firstOrNull { map[it.last()] == Tile.OXYGEN }?.let {
                return it
            }
        }
    }*/
}