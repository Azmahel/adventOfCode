package de.twittgen.aoc.y2023

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.firstMatch

class Day02 : Day<List<Day02.Game>>() {
    override fun String.parse() = lines().mapIndexed {i , game ->
        Game(i +1 ,
            game.split(":")[1]
                .split(";")
                .map { set ->
                    Reveal(
                        redExp.firstMatch(set)?.toInt() ?: 0,
                        greenExp.firstMatch(set)?.toInt() ?: 0,
                        blueExp.firstMatch(set)?.toInt() ?: 0,
                    )
                }.toSet()
        )
    }

    init {
        part1(8,2377) { games ->
            games.filter { it.reveals.maxValues().hasMax(12, 13, 14) }.sumOf { it.id }
        }
        part2(2286, 71220) { game ->
            game.sumOf { it.reveals.maxValues().power }
        }
    }

    private fun Collection<Reveal>.maxValues() =
        Reveal(this.maxOf { it.red }, this.maxOf { it.green }, this.maxOf { it.blue })

    private fun Reveal.hasMax(red: Int, green: Int, blue: Int)  =
        this.red <= red && this.green <= green && this.blue <= blue

    private val redExp = Regex(".* (\\d+) red.*")
    private val blueExp = Regex(".* (\\d+) blue.*")
    private val greenExp = Regex(".* (\\d+) green.*")

    data class Reveal(val red: Int, val green: Int, val blue: Int) {
        val power = red * green * blue
    }

    data class Game(val id: Int, val reveals: Set<Reveal>)

    override val example = """
        Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
        Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
        Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
        Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
        Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
    """.trimIndent()
}

