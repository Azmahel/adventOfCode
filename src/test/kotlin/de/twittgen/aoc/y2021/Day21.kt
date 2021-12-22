package de.twittgen.aoc.y2021

import de.twittgen.aoc.y2019.shared.util.FileUtil
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day21 {
    val input by lazy { FileUtil.readInput("2021/day21").parse() }
    val example = """Player 1 starting position: 4
Player 2 starting position: 8""".parse()

    private fun String.parse() = lines().map { it.last().digitToInt() }.map { Player(it-1) }

    class Game( val players: List<Player>, val dice: Dice)

    class Player(var position: Int, var score:Int =0)

    interface Dice {
        fun roll() : Int
    }

    object DeterministicDice : Dice {
        private var next = 0
        override fun roll(): Int = (next +1).also { next = (next +1) % 100 }
    }

    fun Game.play(): Pair<List<Player>, Int> {
        var numberOfRolls = 0
        while(true) {
            players.forEach { currentPlayer ->
                with(currentPlayer) {
                    val rolled = (dice.roll() + dice.roll() + dice.roll()).also { numberOfRolls +=3 }
                    position = (position + rolled) % 10
                    score += position + 1
                    if(score >= 1000) return players to numberOfRolls
                }
            }
        }
    }

    @Test
    fun example() {
        val result = Game(example, DeterministicDice).play().let {
            it.first.minByOrNull { it.score }!!.score * it.second
        }
        assertEquals(739785,result )
    }

    @Test
    fun example2() {
        val result = example
    }

    @Test
    fun part1() {
        val result = Game(input, DeterministicDice).play().let {
            it.first.minByOrNull { it.score }!!.score * it.second
        }
        println(result)
    }

    @Test
    fun part2() {
        val result = input
        println(result)
    }
}

