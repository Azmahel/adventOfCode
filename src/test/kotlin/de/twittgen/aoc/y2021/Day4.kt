package de.twittgen.aoc.y2021

import de.twittgen.aoc.util.FileUtil
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class Day4 {
    val input by lazy { FileUtil.readInput("2021/day4").parse() }
    val example = """7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1

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
 2  0 12  3  7""".parse()

    data class Board(val id: Int, val numbers : List<List<Pair<Int,Boolean>>>)
    data class BingoGame(val numbers: List<Int>, val boards: List<Board>)
    private fun String.parse(): BingoGame {
        var lines = lines()
        val numbers = lines.first().split(",").map(String::toInt)
        lines = lines.drop(2)
        val boards = lines
            .chunked(6)
            .map {
               if(it.last().isEmpty()) it.dropLast(1) else it
            }.map { board ->
                board.map { line ->
                    line.chunked(3).map { number -> number.trim().toInt() to false }
                }
            }.mapIndexed { i , it -> Board(i, it) }
        return BingoGame(numbers, boards)
    }

    fun BingoGame.takeTurn(): BingoGame {
        val currentNumber = numbers.first()
        val remainingNumbers = numbers.drop(1)
        val newBoards = boards.map { board ->
            Board(board.id, board.numbers.map { row ->
                row.map {  (cell, marked) ->
                    cell to (cell == currentNumber || marked)
                }
            })
        }
        return BingoGame(remainingNumbers, newBoards)
    }

    fun BingoGame.findWinners() = boards.filter { it.isWinner() }

    fun Board.isWinner() = hasWinningRow() || hasWinningColumn()

    fun Board.hasWinningRow() = numbers.any { row -> row.all { (_, marked) -> marked } }

    fun Board.hasWinningColumn() = (0 until numbers.first().size).any { i ->
        numbers.all { row -> row[i].second }
    }

    fun Board.getScore(lastCall: Int) = numbers.flatten().filter { !it.second }.map { it.first }.sum() * lastCall

    fun runGame(game: BingoGame): Int {
        var currentGameState = game
        var winners = emptyList<Board>()
        while(winners.isEmpty()) {
            currentGameState = currentGameState.takeTurn()
            winners = currentGameState.findWinners()
        }
        val winner = winners.first()
        val lastCall = game.numbers.dropLast(currentGameState.numbers.size).last()
        return winner.getScore(lastCall)
    }

    fun runLoosingGame(game: BingoGame): Int {
        var currentGameState = game
        var winners = emptyList<Board>()
        var oldWinners = emptyList<Board>()
        while(winners.size < game.boards.size) {
            oldWinners = winners
            currentGameState = currentGameState.takeTurn()
            winners = currentGameState.findWinners()

        }
        val winner = winners.first { it.id !in oldWinners.map { it.id } }
        val lastCall = game.numbers.dropLast(currentGameState.numbers.size).last()
        return winner.getScore(lastCall)
    }
    @Test
    fun example() {
        val score = runGame(example)
        assertEquals(4512, score)
    }

    @Test
    fun example2() {
        val score = runLoosingGame(example)
        assertEquals(1924, score)
    }

    @Test
    fun part1() {
        val score = runGame(input)
        println(score)
    }

    @Test
    fun part2() {
        val score = runLoosingGame(input)
        println(score)
    }

}
