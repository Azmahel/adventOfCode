package de.twittgen.aoc.y2023

import de.twittgen.aoc.Day


class Day07: Day<CamelPoker>() {
    override fun String.parse() = lines().map { l ->
        l.split(" ").let { (a, b) -> a.map { it.toValue() }  to b.toInt() }
    }

    init {
        part1(6440, 248836197) { it.sortedBy {(hand, _) -> hand.rank() }.score() }
        part2(5905, 251195607) { it.sortedBy {(hand, _) -> hand.jokerRank() }.score() }
    }

    private fun CamelPoker.score() = mapIndexed { i , (_, bid) -> (i+1) * bid }.sum()

    private fun Hand.rank(highCardScore: Long = this.toHighCardScore()) =  groups().let { when {
        it.isFiveOfAKind() -> 6_00_00_00_00_00
        it.isFourOfAKind() -> 5_00_00_00_00_00
        it.isFullHouse() -> 4_00_00_00_00_00
        it.isThreeOfAKind() -> 3_00_00_00_00_00
        it.isTwoPair() -> 2_00_00_00_00_00
        it.isOnePair() -> 1_00_00_00_00_00
        else -> 0L
    } } + highCardScore

    private fun Hand.jokerRank() = jacksAsJokers().let {hand ->
        if (hand.indexOf(0) == -1) hand.rank() else
        hand.toHighCardScore().let {score ->  hand.toSet().maxOf { replacement ->
            hand.map { if(it == 0) replacement else it  }.rank(score)
        } }
    }


    private fun Hand.jacksAsJokers() = map { if(it == faceValues['J']) 0 else it }
    private fun Hand.toHighCardScore() =
        joinToString("") { it.toString().padStart(2, '0') }.toLong()
    private fun Collection<List<Int>>.isFiveOfAKind() = size == 1
    private fun Collection<List<Int>>.isFourOfAKind() = any { it.size == 4 }
    private fun Collection<List<Int>>.isFullHouse() = size == 2 && any { it.size == 3 }
    private fun Collection<List<Int>>.isThreeOfAKind() = size == 3 && any { it.size == 3 }
    private fun Collection<List<Int>>.isTwoPair() = size == 3 && filter { it.size == 2 }.size == 2
    private fun Collection<List<Int>>.isOnePair() = size == 4



    private fun Hand.groups() = groupBy { it }.values
    private fun Char.toValue() = when {
        isDigit() -> digitToInt()
        else -> faceValues[this]!!
    }

    private val faceValues = mapOf('T' to 10,'J' to 11, 'Q' to 12, 'K' to 13 , 'A' to 14)

    override val example = """
        32T3K 765
        T55J5 684
        KK677 28
        KTJJT 220
        QQQJA 483
    """.trimIndent()

}
typealias Hand = List<Int>
typealias CamelPoker = List<Pair<Hand, Int>>