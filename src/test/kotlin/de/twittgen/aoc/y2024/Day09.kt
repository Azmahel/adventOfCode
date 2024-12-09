package de.twittgen.aoc.y2024

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.replaceAt
import de.twittgen.aoc.util.times

class Day09: Day<List<Day09.Block>>() {
    override fun String.parse() = mapIndexed { i, c -> if (i%2==0) File(i/2,c.digitToInt()) else Empty(c.digitToInt())  }

    init {
        part1(1928, 6262891638328) {
            it.expand().moveFromEnd().checksum()
        }
        part2(2858, 6287317016845) {
            it.moveFilesFromEnd().expand().checksum()
        }
    }

    private fun List<Block>.checksum() = mapIndexed { i, it -> if (it is File) i*it.id.toLong() else 0  }.sum()

    private fun List<Block>.moveFilesFromEnd(): List<Block> {
        var current = this
        reversed().filterIsInstance<File>().forEach { block ->
            var (c1,c2) = 0 to current.lastIndexOf(block)
            while (c1<c2) {
                val a = current[c1]
                if (a is Empty && a.size >= block.size) {
                    current = current.toMutableList().replaceAt(c2, Empty(block.size))
                        .let { it.take(c1) + block + diff(a,block) + it.drop(c1 +1) }
                        .also { c1 = c2 }
                }
                c1++
            }
        }
        return current
    }

    private fun diff(a:Empty, b:File) = if(a.size <= b.size) emptyList() else listOf(Empty(a.size - b.size))

    private fun List<Block>.expand()= flatMap { when(it) {
        is Empty -> listOf(Empty(1)).times(it.size)
        is File  -> listOf(File(it.id, 1)).times(it.size)
    }}

    private fun List<Block>.moveFromEnd(): List<Block> {
        val current = toMutableList()
        var (c1,c2) = 0 to lastIndex
        while (c1<c2) {
            while (current[c1] is File) c1 += 1
            while (current[c2] is Empty) c2 -= 1
            if (c1 >= c2) break
            current.replaceAt(c1, current[c2])
            current.removeAt(c2).also { c2 -= 1 }
        }
        return current
    }

    sealed class Block
    private data class Empty(val size: Int) : Block()
    private data class File(val id: Int, val size: Int) : Block()

    override val example = "2333133121414131402"
}