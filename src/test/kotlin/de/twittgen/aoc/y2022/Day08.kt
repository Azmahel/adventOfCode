package de.twittgen.aoc.y2022

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.*

class Day08 : Day<List<List<Int>>>() {
    override fun String.parse() = lines().map { it.map { c ->  c.digitToInt() } }

    init {
        part1(21, 1719) { it.findVisibleTrees().size }
        part2(8, 590824) { it.flatMapIndexed { x , row -> row.mapIndexed { y, tree ->
                it.getSightLinesAt(x,y).map { line -> line.takeUntil { other -> other >= tree }.size }.product()
        } }.maxOrNull()!! }
    }

    private fun List<List<Int>>.findVisibleTrees() = visible() + columns().visible().map(Point2D::transpose)

    private fun List<List<Int>>.visible() = flatMapIndexed { x, it -> it.find().map { y -> Point2D(x,y) } }.toSet()

    private fun List<Int>.find() = lookAt() + reversed().lookAt().map { lastIndex - it }

    private fun List<Int>.lookAt() = withIndex()
        .filter { (y, it) -> y==0 || it > take(y).maxOrNull()!! }.map { (y , _) -> y }.toSet()

    private fun <T> List<List<T>>.getSightLinesAt(x : Int, y:Int) = listOf(
        column(y).take(x).reversed(),
        column(y).drop(x+1),
        get(x).take(y).reversed(),
        get(x).drop(y+1)
    )

    override val example = """
        30373
        25512
        65332
        33549
        35390
    """.trimIndent()
}