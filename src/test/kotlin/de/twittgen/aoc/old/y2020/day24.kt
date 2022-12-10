package de.twittgen.aoc.old.y2020

import de.twittgen.aoc.util.FileUtil
import de.twittgen.aoc.util.second
import de.twittgen.aoc.old.y2020.day24.Direction
import de.twittgen.aoc.old.y2020.day24.Direction.*
import org.junit.jupiter.api.Test
import java.lang.IllegalArgumentException

class day24 {
    val input = FileUtil.readInput("2020/day24")
    val example = """
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

    fun parseInput(s :String): List<List<Direction>> {
       return  s.lines().map {
            var remainder = it
            val result = mutableListOf<Direction>()
            while(remainder.isNotEmpty()){
                result.add(
                    when(remainder.first()) {
                        'e' -> E
                        'w' -> W
                        's' -> when(remainder.second()) {
                            'e' -> { remainder = remainder.drop(1);SE }
                            'w' -> { remainder = remainder.drop(1); SW }
                            else -> throw IllegalArgumentException()
                        }
                        'n' -> when(remainder.second()) {
                            'e' -> { remainder = remainder.drop(1); NE }
                            'w' -> { remainder = remainder.drop(1); NW }
                            else -> throw IllegalArgumentException()
                        }
                    else -> throw IllegalArgumentException()
                })
                remainder = remainder.drop(1)
            }
           result
        }
    }

    @Test
    fun example() {
        val instructions = parseInput(example)
        val blackTiles = perform(instructions)
        assert(
            blackTiles.count() == 10
        )
    }

    @Test
    fun part1() {
        val instructions = parseInput(input)
        val blackTiles = perform(instructions)
        println(blackTiles.count())
    }

    @Test
    fun part2() {
        val instructions = parseInput(input)
        val blackTiles = perform(instructions)
        val result = playGameOfLife(blackTiles)
        println(result.count())
    }

    fun perform(instructions: List<List<Direction>>): Set<Pair<Int, Int>> {
        val blackTiles = mutableSetOf<Pair<Int,Int>>()
        for( instruction in instructions) {
            var position = 0 to 0
            instruction.forEach { position = with(position) {first + it.dx to second + it.dy } }
            if(blackTiles.contains(position)) blackTiles.remove(position) else blackTiles.add(position)
        }
        return blackTiles
    }

    tailrec fun playGameOfLife(tiles : Set<Pair<Int,Int>>, moves: Int = 100): Set<Pair<Int, Int>> {
        if (moves == 0) return tiles
        val relevantTiles = tiles.flatMap { it.getAdjacent() }.toSet()
        val next = relevantTiles.map {it to it.getAdjacent().filter { it in tiles } }.filter { (tile,adjacent) ->
            if(tile in tiles) { adjacent.size in 1..2 } else { adjacent.size == 2 }
        }.map { it.first }.toSet()
        return playGameOfLife(next, moves-1)
    }

private fun String.second(): Char = toList().second()
    enum class Direction(val dx: Int, val dy:Int) {
        E(0,2),
        SE(1,1),
        NE(-1,1),
        W(0,-2),
        SW(1,-1),
        NW(-1,-1)
    }
}

private fun Pair<Int, Int>.getAdjacent(): List<Pair<Int,Int>> = Direction.values().map { first + it.dx to second + it.dy }
