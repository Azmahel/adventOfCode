package de.twittgen.aoc.y2024

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.Point2D
import de.twittgen.aoc.util.emptyLine

class Day13 : Day<List<LES>>() {
    override fun String.parse() = split(emptyLine).map { b -> b.lines().let { (a, b, z) ->
        a.toButton().let { a -> b.toButton().let { b -> listOf(listOf(a.x, b.x), listOf(a.y, b.y)) } } to z.toPrice()
    } }

    private val priceRegex = Regex("Prize: X=(\\d+), Y=(\\d+)")
    private fun String.toPrice() = priceRegex.toPoint2D(this)
    private val buttonRegex = Regex("Button [AB]: X\\+(\\d+), Y\\+(\\d+)")
    private fun String.toButton() = buttonRegex.toPoint2D(this)
    private fun Regex.toPoint2D(s: String) =
        matchEntire(s)!!.groupValues.let { (_, x, y) -> Point2D(x.toInt(), y.toInt()) }

    init {
        part1(480) { it.sumOf { it.bruteForce() } }
    }

    private fun LES.bruteForce() = let { (m, v) -> (0..100).flatMap { pa -> (0..100).mapNotNull { pb ->
        if (m[0][0] * pa + m[0][1] * pb == v.x && m[1][0] * pa + m[1][1] * pb == v.y) {
            (3 * pa) + pb
        } else {
            null
        }
    } }.minOrNull() ?: 0 }


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

private typealias LES = Pair<List<List<Int>>, Point2D>