package de.twittgen.aoc.y2022

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.column
import de.twittgen.aoc.util.columns
import de.twittgen.aoc.util.product
import de.twittgen.aoc.util.takeUntil

class Day8 : Day<Int, Int, List<List<Int>>>() {
    override fun String.parse() = lines().map { it.map { c ->  c.digitToInt() } }

    init {
        part1(21, 1719) {
           findVisibleTrees().size
        }
        part2(8, 590824) {
            mapIndexed { x , row ->
                row.mapIndexed {
                    y, tree -> getSightLinesAt(x,y).map { line -> line.takeUntil { it >= tree }.size }.product()
                }.maxOrNull()!!
            }.maxOrNull()!!
        }
    }

    private fun List<List<Int>>.findVisibleTrees(): Set<Pair<Int, Int>> {
        val visibleForRows = flatMapIndexed { x, it -> it.findVisibleIndices().map { y -> x to y } }.toSet()
        val visibleForColumns = columns().flatMapIndexed { y, it -> it.findVisibleIndices().map { x -> x to y } }.toSet()
        return visibleForRows + visibleForColumns
    }

    private fun List<Int>.findVisibleIndices() = lookAt() + reversed().lookAt().map { lastIndex - it }

    private fun List<Int>.lookAt() = withIndex()
        .filter { (y, it) -> y==0 || it > take(y).maxOrNull()!! }.map { (y , _) -> y }
        .toSet()


    private fun <T> List<List<T>>.getSightLinesAt(x : Int, y:Int) = listOf(
        column(y).take(x).reversed(),
        column(y).drop(x+1),
        get(x).take(y).reversed(),
        get(x).drop(y+1),
    )

    override val example = """
        30373
        25512
        65332
        33549
        35390
    """.trimIndent()
}