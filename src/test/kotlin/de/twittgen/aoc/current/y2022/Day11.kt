package de.twittgen.aoc.current.y2022

import de.twittgen.aoc.current.Day
import de.twittgen.aoc.current.y2022.Day11.Monkey
import java.lang.IllegalStateException

class Day11: Day<Map<Int, Monkey>>() {
    override fun String.parse() = split("\n\n").mapIndexed { i, s -> i to s.toMonkey() }.toMap()

    init {
        mutableModel = true
        part1(10605, 55944) { it.doRounds(20) { this / 3} }
        part2(2713310158, 15117269860) {
            val mod = it.values.map { m -> m.checkAgainst }.fold(1L) {a,b -> a*b}
            it.doRounds(10000) { this % mod }
        }
    }

    private fun String.toMonkey() : Monkey = with(lines().drop(1)) {
        Monkey(
            get(0).dropWhile { it != ':' }.drop(2).split(", ").map(String::toLong),
            get(1).dropWhile { it != '=' }.drop(2).toOperation(),
            get(2).split(" ").last().toLong(),
            get(3).last().digitToInt() to get(4).last().digitToInt()
        )
    }

    private fun String.toOperation() : (Long) -> Long = split(" ").let { (_, o, b) ->
        when (o) {
            "*" -> {old ->  old * (b.toLongOrNull() ?: old) }
            "+" -> {old -> old + (b.toLongOrNull() ?: old) }
            else -> throw IllegalStateException()
        }
    }

    private fun Map<Int, Monkey>.doRounds(times: Int, relieved: Long.() -> Long): Long {
        repeat(times) { doRound(relieved) }
        return values.map { it.business.toLong() }.sortedDescending().take(2).fold(1) { a, b -> a*b}
    }

    private fun Map<Int,Monkey>.doRound(relieved: Long.()-> Long) =
        keys.sorted().forEach { get(it)!!.doTurn(relieved).forEach { (target, item) -> get(target)!!.items += item } }

    data class Monkey(
        var items: List<Long>,
        val inspect : (Long) -> Long,
        val checkAgainst : Long,
        val targets: Pair<Int, Int>
    ) {
        var business = 0
        fun doTurn(relieved : Long.()->Long) = items.map { item ->
            inspect(item).relieved()
                .let { (if (test(it)) targets.first else targets.second) to it }
                .also { business ++ }
                .also { items = emptyList() }
        }
        fun test(i : Long ) = i % checkAgainst == 0L
    }

    override val example = """
Monkey 0:
  Starting items: 79, 98
  Operation: new = old * 19
  Test: divisible by 23
    If true: throw to monkey 2
    If false: throw to monkey 3

Monkey 1:
  Starting items: 54, 65, 75, 74
  Operation: new = old + 6
  Test: divisible by 19
    If true: throw to monkey 2
    If false: throw to monkey 0

Monkey 2:
  Starting items: 79, 60, 97
  Operation: new = old * old
  Test: divisible by 13
    If true: throw to monkey 1
    If false: throw to monkey 3

Monkey 3:
  Starting items: 74
  Operation: new = old + 3
  Test: divisible by 17
    If true: throw to monkey 0
    If false: throw to monkey 1
""".trimIndent()
}

