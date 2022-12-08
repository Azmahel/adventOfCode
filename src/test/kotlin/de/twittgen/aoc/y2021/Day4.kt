package de.twittgen.aoc.y2021

import de.twittgen.aoc.Day

class Day4 : Day<Int, Int, Day4.BingoGame>() {
    override val example = """7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1

22 13 17 11  0
 8  2 23  4 24
21  9 14 16  7
 6 10  3 18  5
 1 12 20 15 19

 3 15  0  2 22
 9 18 13 17  5
19  8  7 25 23
20 11 10 24  4
14 21 16 12  6

14 21 17 24  4
10 16 15  9 19
18  8 23 26 20
22 11 13  6  5
 2  0 12  3  7"""

    override fun String.parse() = BingoGame(
        lines().first().split(",").map(String::toInt),
        lines()
            .drop(2)
            .chunked(6)
            .map { it.filter(String::isNotEmpty) }
            .mapIndexed { i, board ->
                Board(i, board.map { line -> line.chunked(3).map { number -> number.trim().toInt() to false } })
            }
    )

    init {
        part1(4512, ) { this.runGame(getFirstWinner).run { first.getScore(second) } }
        part2(1924, ) { this.runGame(getLastWinner).run { first.getScore(second) } }
    }


    private fun BingoGame.takeTurn(): BingoGame = BingoGame(
        numbers.drop(1),
        boards.map { board ->
            board.copy(
                numbers = board.numbers
                    .map { row -> row.map { (cell, marked) -> cell to (cell == numbers.first() || marked) } }
            ) },
            numbers.first()
        )

    private val getFirstWinner: (BingoGame, BingoGame) -> Board?  = { _, new ->  new.findWinners().firstOrNull()}
    private val getLastWinner: (BingoGame, BingoGame) -> Board?  = { old, new ->
        if(new.boards.size == new.findWinners().size) {
            new.findWinners().first { it.id !in old.findWinners().map(Board::id) }
        } else {
            null
        }
    }

    private fun BingoGame.runGame(winnerFunction: (BingoGame, BingoGame) -> Board?) : Pair<Board, Int> =
        takeTurn().let { new ->
            winnerFunction(this, new)?.let { winner -> winner to new.lastCall } ?: new.runGame(winnerFunction)
        }

    data class Board(val id: Int, val numbers : List<List<Pair<Int,Boolean>>>) {
        fun hasWinningRow() = numbers.any { row -> row.all { (_, marked) -> marked } }
        fun hasWinningColumn() = (0 until numbers.first().size).any { i -> numbers.all { row -> row[i].second } }
        fun getScore(lastCall: Int) = numbers.flatten().filter { !it.second }.sumOf { it.first } * lastCall
    }

    data class BingoGame(val numbers: List<Int>, val boards: List<Board>, val lastCall : Int = 0) {
        fun findWinners() = boards.filter { it.hasWinningRow() || it.hasWinningColumn() }
    }
}
