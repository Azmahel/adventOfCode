package de.twittgen.aoc.y2024

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.emptyLine
import de.twittgen.aoc.util.isWhole
import de.twittgen.aoc.util.lcm

class Day13 : Day<List<LES>>() {
    override fun String.parse() = split(emptyLine).map { b -> b.lines().let { (a, b, z) ->
        a.toButton().let { a -> b.toButton().let { b -> listOf(listOf(a.first, b.first), listOf(a.second, b.second)) } } to z.toPrice()
    } }

    private val priceRegex = Regex("Prize: X=(\\d+), Y=(\\d+)")
    private fun String.toPrice() = priceRegex.toPoint2D(this)
    private val buttonRegex = Regex("Button [AB]: X\\+(\\d+), Y\\+(\\d+)")
    private fun String.toButton() = buttonRegex.toPoint2D(this)
    private fun Regex.toPoint2D(s: String) =
        matchEntire(s)!!.groupValues.let { (_, x, y) -> x.toLong() to y.toLong() }

    init {
        part1(480, 29877) { it.sumOf { it.solve() } }
        part2(875318608908, 99423413811305) { it.sumOf{ it.expand().solve() }}
    }

    private fun LES.expand() = first to second.let {(x, y) -> 10000000000000 + x to 10000000000000 + y}

    private fun LES.solve() = let { (m, v) ->
        val c = lcm(m[0][0], m[1][0])
        val bP = (1.0*(v.first * c / m[0][0])-(v.second * c / m[1][0])) / ((m[0][1] * c / m[0][0]) - (m[1][1] * c / m[1][0]))
        val aP = (v.first - m[0][1] * bP)/m[0][0]
        if(aP.isWhole() && bP.isWhole()) (3 * aP + bP).toLong() else 0
    }

    override val example = """
        Button A: X+94, Y+34
        Button B: X+22, Y+67
        Prize: X=8400, Y=5400

        Button A: X+26, Y+66
        Button B: X+67, Y+21
        Prize: X=12748, Y=12176

        Button A: X+17, Y+86
        Button B: X+84, Y+37
        Prize: X=7870, Y=6450

        Button A: X+69, Y+23
        Button B: X+27, Y+71
        Prize: X=18641, Y=10279
    """.trimIndent()
}

private typealias LES = Pair<List<List<Long>>, Pair<Long, Long>>