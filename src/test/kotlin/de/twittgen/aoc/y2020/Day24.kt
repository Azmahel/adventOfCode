package de.twittgen.aoc.y2020

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.FileUtil
import de.twittgen.aoc.util.mapLines
import de.twittgen.aoc.y2020.Day24.Direction
import de.twittgen.aoc.y2020.Day24.Direction.*
import java.lang.IllegalArgumentException

class Day24 : Day<List<List<Direction>>>() {
    val input = FileUtil.readInput("2020/day24")

    override fun String.parse() = mapLines {
        val remainder = it.toMutableList()
        val result = mutableListOf<Direction>()
        while(remainder.isNotEmpty()){
            result.add(when(remainder.removeFirst()) {
                'e' -> E
                'w' -> W
                's' -> when(remainder.removeFirst()) {
                    'e' -> SE
                    'w' -> SW
                    else -> throw IllegalArgumentException()
                }
                'n' -> when(remainder.removeFirst()) {
                    'e' ->  NE
                    'w' ->  NW
                    else -> throw IllegalArgumentException()
                }
                else -> throw IllegalArgumentException()
            })
        }
       result
    }

    init {
        part1(10, 275) { it.perform().count() }
        part2(2208, 3537) { it.perform().playGameOfLife().count() }
    }

    private fun List<List<Direction>>.perform(): Set<Pair<Int, Int>> {
        val blackTiles = mutableSetOf<Pair<Int,Int>>()
        for(instruction in this) {
            var position = 0 to 0
            instruction.forEach { position = with(position) { first + it.dx to second + it.dy } }
            if(blackTiles.contains(position)) blackTiles.remove(position) else blackTiles.add(position)
        }
        return blackTiles
    }

    private tailrec fun Set<Pair<Int, Int>>.playGameOfLife(moves: Int = 100): Set<Pair<Int, Int>> {
        if (moves == 0) return this
        val relevantTiles = flatMap { it.getAdjacent() }.toSet()
        val next = relevantTiles.map {it to it.getAdjacent().filter { c ->  c in this } }.filter { (tile,adjacent) ->
            if(tile in this) { adjacent.size in 1..2 } else { adjacent.size == 2 }
        }.map { it.first }.toSet()
        return next.playGameOfLife( moves - 1)
    }

    enum class Direction(val dx: Int, val dy:Int) {
        E(0,2), SE(1,1), NE(-1,1), W(0,-2), SW(1,-1), NW(-1,-1)
    }

    private fun Pair<Int, Int>.getAdjacent(): List<Pair<Int,Int>> = entries.map { first + it.dx to second + it.dy }

    override val example = """
        sesenwnenenewseeswwswswwnenewsewsw
        neeenesenwnwwswnenewnwwsewnenwseswesw
        seswneswswsenwwnwse
        nwnwneseeswswnenewneswwnewseswneseene
        swweswneswnenwsewnwneneseenw
        eesenwseswswnenwswnwnwsewwnwsene
        sewnenenenesenwsewnenwwwse
        wenwwweseeeweswwwnwwe
        wsweesenenewnwwnwsenewsenwwsesesenwne
        neeswseenwwswnwswswnw
        nenwswwsewswnenenewsenwsenwnesesenew
        enewnwewneswsewnwswenweswnenwsenwsw
        sweneswneswneneenwnewenewwneswswnese
        swwesenesewenwneswnwwneseswwne
        enesenwswwswneneswsenwnewswseenwsese
        wnwnesenesenenwwnenwsewesewsesesew
        nenewswnwewswnenesenwnesewesw
        eneswnwswnwsenenwnwnwwseeswneewsenese
        neswnwewnwnwseenwseesewsenwsweewe
        wseweeenwnesenwwwswnew
    """.trimIndent()
}

