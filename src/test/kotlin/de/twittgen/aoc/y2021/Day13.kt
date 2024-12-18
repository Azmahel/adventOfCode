package de.twittgen.aoc.y2021

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.columns
import de.twittgen.aoc.util.emptyLine
import de.twittgen.aoc.util.mapLines
import de.twittgen.aoc.y2021.Day13.Fold
import de.twittgen.aoc.y2021.Day13.FoldDirection.X
import de.twittgen.aoc.y2021.Day13.FoldDirection.Y


class Day13 : Day<Pair<Paper, List<Fold>>>() {
    override fun String.parse() = split(emptyLine)
        .let { (rawMarks, rawFolds) -> rawMarks.parseMarks() to rawFolds.parseFolds() }

    private fun String.parseFolds() = mapLines { line ->
        line.takeLastWhile { it != ' ' }.split("=").let {(a,b) ->  Fold(FoldDirection.from(a), b.toInt()) }
    }
    private fun String.parseMarks() = mapLines { line -> line.split(",").let { (a,b) ->  a.toInt() to b.toInt() } }.toSet()

    init {
        part1(17, 661) { (paper, folds) -> paper.foldAt(folds.first()).size }
        part2(exampleExpected, part2Expected) { (paper, folds) -> folds.fold(paper) { p, f -> p.foldAt(f) }.print() }
    }

    enum class FoldDirection { X, Y; companion object { fun from(s: String) = if(s == "x") X else Y } }

    data class Fold(val direction: FoldDirection, val at: Int)

    private fun Paper.foldAt(fold: Fold) = map { when (fold.direction) {
        X -> it.first.foldAt(fold.at) to it.second
        Y ->  it.first  to it.second.foldAt(fold.at)
    } }.toSet()

    private fun Paper.print() =
        "\n" + (0..maxByOrNull { it.first }!!.first).map { x -> (0..maxByOrNull { it.second }!!.second).map{ y ->
            if (x to y in this) '®' else ' '
        } }.columns().joinToString("\n") { it.joinToString("") }

    private fun Int.foldAt( other: Int) = if(this <= other) this else other - (this -other)

    override val example = """
       6,10
       0,14
       9,10
       0,3
       10,4
       4,11
       6,0
       6,12
       4,1
       0,13
       10,12
       3,4
       3,0
       8,4
       1,10
       2,14
       8,10
       9,0
       
       fold along y=7
       fold along x=5
   """.trimIndent()
}
private val exampleExpected = """
    
        ®®®®®
        ®   ®
        ®   ®
        ®   ®
        ®®®®®
     """.trimIndent()
private val part2Expected = """
    
        ®®®  ®®®® ®  ® ®    ®  ®  ®®  ®®®® ®®® 
        ®  ® ®    ® ®  ®    ® ®  ®  ® ®    ®  ®
        ®  ® ®®®  ®®   ®    ®®   ®    ®®®  ®  ®
        ®®®  ®    ® ®  ®    ® ®  ®    ®    ®®® 
        ®    ®    ® ®  ®    ® ®  ®  ® ®    ®   
        ®    ®    ®  ® ®®®® ®  ®  ®®  ®    ®   
    """.trimIndent()
private typealias Paper = Set<Pair<Int, Int>>


