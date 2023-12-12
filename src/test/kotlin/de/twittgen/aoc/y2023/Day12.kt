package de.twittgen.aoc.y2023

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.expand
import de.twittgen.aoc.util.times

class Day12: Day<List<Pair<String, List<Int>>>>() {
    override fun String.parse() = lines().map {
        it.split(" ").let { (a,b) -> a to b.split(",").map(String::toInt) }
    }

    init {
        part1(21, 7843) {
            it.sumOf { (lines, groups) -> lines.fit(groups) }
        }
        part2(525152, 7843) { it
            .map { (l, g) -> l.expand(5, "?") to g.times(5)}
            .sumOf { (lines, groups) -> lines.fit(groups) }
        }
    }

    private val cache = hashMapOf<Pair<String, List<Int>>, Long>()
    private fun String.fit(groups: List<Int> ) : Long {
        if(groups.isEmpty())
            return if ('#' in this) 0 else 1
        if(this.isEmpty())
            return 0

        return cache.getOrPut(this to groups) {
            var result = 0L
            if (this.fitsStart(groups.first()))
                result += drop(groups.first() + 1).fit(groups.drop(1))
            if (first() != '#')
                result += drop(1).fit(groups)
            result
        }
    }
    private fun String.fitsStart(group: Int) =
        group <= length && '.' !in take(group) && ( group == length || get(group) != '#')


    override val example = """
        ???.### 1,1,3
        .??..??...?##. 1,1,3
        ?#?#?#?#?#?#?#? 1,3,1,6
        ????.#...#... 4,1,1
        ????.######..#####. 1,6,5
        ?###???????? 3,2,1
    """.trimIndent()
}