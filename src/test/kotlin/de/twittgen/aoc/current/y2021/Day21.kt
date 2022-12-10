package de.twittgen.aoc.current.y2021

import de.twittgen.aoc.Day
import de.twittgen.aoc.current.y2021.Day21.Player

class Day21: Day<List<Player>>() {
    override fun String.parse() = lines().map { it.last().digitToInt() }.map { Player(it-1) }

    init {
        mutableModel = true
        part1(739785, 841794) {players ->
            Game(players, DeterministicDice).play().let { it.players.minOf(Player::score) * it.totalRolls }
        }
    }

    class Game(val players: List<Player>, private val dice: Dice) {
        var totalRolls = 0
            private set
        fun play(): Game {
            while(true) {
                players.forEach { currentPlayer ->
                    with(currentPlayer) {
                        val rolled = (dice.roll() + dice.roll() + dice.roll()).also { totalRolls +=3 }
                        position = (position + rolled) % 10
                        score += position + 1
                        if(score >= 1000) return this@Game
                    }
                }
            }
        }
    }

    class Player(var position: Int, var score:Int =0)

    interface Dice { fun roll() : Int }

    object DeterministicDice : Dice {
        private var next = 0
        override fun roll(): Int = (next +1).also { next = (next +1) % 100 }
    }

    override val example = """
        Player 1 starting position: 4
        Player 2 starting position: 8
    """.trimIndent()
}

