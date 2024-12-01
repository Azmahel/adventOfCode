package de.twittgen.aoc.y2021

import de.twittgen.aoc.Day
import de.twittgen.aoc.y2021.Day21.Player

class Day21: Day<Pair<Player, Player>>() {
    override fun String.parse() = lines().map { it.last().digitToInt() }.map { Player(it-1) }.let { (a,b) ->  a to b }

    init {
        mutableModel = true
        part1(739785, 841794) {(p1, p2 ) -> GameState(p1.copy(), p2.copy()).play() }
        part2(444356092776315, 306719685234774) {(p1,p2) ->
            playWithMultiverse(GameState(p1.copy(),p2.copy()), DiracAdv, 21)
        }
    }

    data class GameState(val p1: Player, val p2: Player) {
        val players = listOf(p1, p2)
    }

    private fun GameState.play(): Int {
        var totalRolls = 0
        while(true) {
            players.forEach { currentPlayer ->
                with(currentPlayer) {
                    val rolled = (DeterministicDice.roll() + DeterministicDice.roll() + DeterministicDice.roll())
                        .also { totalRolls +=3 }
                    position = (position + rolled) % 10
                    score += position + 1
                    if(score >= 1000) return players.minOf(Player::score) * totalRolls
                }
            }
        }
    }

    private fun playWithMultiverse(start : GameState, adv: Advancer, playTo: Int) : Long {
        var multiverses = listOf(start to 1L)
        var p1Wins = 0L
        var p2Wins = 0L
        var p1Turn = true
        while(multiverses.isNotEmpty()) {
           multiverses  =  multiverses.flatMap { (gamestate, count) ->
               if(gamestate.p1.score >= playTo){
                   p1Wins += count
                   emptyList()
               } else if (gamestate.p2.score >= playTo) {
                   p2Wins += count
                   emptyList()
               } else {
                   adv(gamestate, p1Turn).map { it.first to count*it.second }
               }
           }.groupBy { it.first }.mapValues { (_,v) -> v.sumOf { it.second } }.toList()
           p1Turn = !p1Turn
        }
        return maxOf(p1Wins, p2Wins)
    }



    data class Player(var position: Int, var score:Int =0)


    private sealed class Advancer {
        protected fun GameState.next(p1turn: Boolean, rolled: Int) : GameState {
            val nextP = (if (p1turn) p1 else p2).copy().apply {
                position = (position + rolled) % 10
                score += position + 1
            }
            return GameState(
                if (p1turn) nextP else p1,
                if (p1turn) p2 else nextP
            )
        }
        abstract operator fun invoke(g: GameState, p1turn: Boolean) :List<Pair<GameState, Int>>
    }


    private data object DiracAdv : Advancer() {
        private val dist = (1..3).flatMap { a -> (1..3).flatMap { b -> (1..3).map { c ->
            a+b+c to "$a$b$c"
        } } }.groupBy { it.first }.mapValues { (_,v) -> v.size }

        override fun invoke(g: GameState, p1turn: Boolean) = dist.map { (sum, count) -> g.next(p1turn, sum) to count }
    }

    object DeterministicDice {
        private var next = 0
        fun roll(): Int = (next +1).also { next = (next +1) % 100 }
    }

    override val example = """
        Player 1 starting position: 4
        Player 2 starting position: 8
    """.trimIndent()
}

