package de.twittgen.aoc.y2020

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.emptyLine
import de.twittgen.aoc.y2020.Day20.Tile
import de.twittgen.aoc.y2020.Day20.Tile.Direction.*
import kotlin.math.roundToInt
import kotlin.math.sqrt

class Day20 : Day<List<Tile>>() {

    override fun String.parse() = split(emptyLine).map { it.parseTile() }

    private val tileNumber = Regex("Tile (\\d+):")
    private fun String.parseTile(): Tile {
        val (id) = tileNumber.matchEntire(lines().first())!!.destructured
        val body = lines().drop(1)
       return Tile(
           id.toInt(),
           Borders(
               body.first(),
               body.map { it.last() }.joinToString(""),
               body.last(),
               body.map { it.first() }.joinToString("")
           ),
           body.drop(1).dropLast(1).mapIndexed { x, it -> it.drop(1).dropLast(1).mapIndexedNotNull { y, c ->
               if(c == '#') x to y else null
           }}.flatten()
       )
    }

    init {
        part1(20899048083289, 18262194216271) { tiles ->
            val matching = tiles.map {  it to (tiles - it).filter { other -> it.fits(other) } }
            val cornerCandidates = matching.filter { (_, matching ) -> matching.size ==2 }
            cornerCandidates.fold(1L) { acc,x -> x.first.id.toLong() * acc }
        }
        part2(273, 2023) { tiles ->
            val matching = tiles.map { it to (tiles - it).filter { other -> it.fits(other) } }
            val result = assemble(matching)
            val image = result.join()
            val seaMonsters = image.countMonster()
            image.count() - (seaMonsters* seaMonster.count())
        }
    }

    data class Tile(val id : Int, val borders : Borders, val body: Image, val size: Int = 10) {
        fun fits(other: Tile) = borders.all.any{ b -> other.fits(b) }
        fun fits(other: String) = borders.all.any { b-> b == other || b == other.reversed() }

        fun transformToFit(key: String, from: Direction): Tile {
            var current = this
            var lock = current.getBorder(from)
            while(lock != key && lock != key.reversed()) {
                current = current.rotate()
                lock = current.getBorder(from)
            }
            if(lock != key) current = current.flip(from)
            return current
        }

        private fun getBorder(direction: Direction) =when(direction) {
            N -> borders.n
            E -> borders.e
            S -> borders.s
            W -> borders.w
        }

        fun rotate() =  this.copy(borders = with(borders) { Borders(e,s.reversed(),w,n.reversed()) }, body = body.rotate(size-2))

        fun flip(axis: Direction): Tile {
            val newBorders = when(axis) {
                N,S -> with(borders) { Borders(n.reversed(), w, s.reversed(), e) }
                E,W -> with(borders) { Borders(s, e.reversed(), n, w.reversed()) }
            }
            return copy(borders = newBorders, body = body.flip(axis, size-2))
        }

        enum class Direction { N,E,S,W }
    }

    data class Borders(val n: String, val e: String, val s: String, val w: String) { val all = listOf(n,e,s,w) }

    private fun Image.countMonster(): Int {
        val rot : Image.() -> Image = { rotate(maxOf { it.first }) }
        val flipH: Image.() -> Image = { flip(E, maxOf { it.first }) }
        val flipV: Image.() -> Image = { flip(N, maxOf { it.first }) }
        val possibleImages = listOf(
            this, rot(), rot().rot(), rot().rot().rot(), flipH().rot(), flipH().rot().rot(), flipH().rot().rot().rot(),
            flipV().rot(), flipV().rot().rot(), flipV().rot().rot().rot(), flipH().flipV().rot(),
            flipH().flipV().rot().rot(), flipH().flipV().rot().rot().rot()
        ).distinct()
        return possibleImages.maxOf { it.countMonsters() }
    }

    private val seaMonster= """
                          # 
        #    ##    ##    ###
         #  #  #  #  #  #   
    """.trimIndent().lines()
        .mapIndexed { x, s -> s.mapIndexedNotNull { y, it -> if(it == '#') x to y else null } }.flatten()

    private fun Image.countMonsters(): Int {
        val max = maxOf { it.first }
        return (0..max).flatMap { x-> (0..max).map { y ->
            seaMonster.all { (sx, sy) -> firstOrNull { it == (x + sx) to (y + sy) } != null }
        } }.count { it }
    }

    private fun Map<Pair<Int,Int>,Tile>.join(): Image {
        val max = keys.maxOf { it.first }
        return (0..max).flatMap { x-> (0..max).flatMap { y-> with(get(x to y)!!) {
            body.map { (ix, iy) -> (ix + (x * (size-2))) to (iy + (y * (size-2))) }
        } } }
    }

    private fun assemble(matching: List<Pair<Tile, List<Tile>>>): Map<Pair<Int, Int>, Tile> {
        val start = matching.first { it.second.size == 2 }.rotateToFitTopCorner()
        val result = mutableMapOf((0 to 0) to start)
        val dimension = sqrt(matching.size.toDouble()).roundToInt()
        (0 until dimension).forEach { x -> (0 until dimension).forEach { y -> if (x != 0 || y != 0) {
            result[x to y] = result.findTile(x, y, matching)
        } } }
        return result
    }

    private fun MutableMap<Pair<Int, Int>, Tile>.findTile(x: Int, y: Int, m: List<Pair<Tile, List<Tile>>>) =
        if (y != 0) {
            val last = this[x to y - 1]!!
            m.first { it.first.id == last.id }.second.first { it.fits(last.borders.e) }.transformToFit(last.borders.e, W)
        } else {
            val last = this[x - 1 to y]!!
            m.first { it.first.id == last.id }.second.first { it.fits(last.borders.s) }.transformToFit(last.borders.s, N)
        }

    private fun  Pair<Tile, List<Tile>>.rotateToFitTopCorner(): Tile {
        var current = first
        while( second.none { it.fits(current.borders.e) } || second.none { it.fits(current.borders.s)} ) {
            current = if(second.none { it.fits(current.borders.w) } || second.none { it.fits(current.borders.s)})
                current.flip(E) else current.rotate()
        }
        return current
    }

    override val example = """
        Tile 2311:
        ..##.#..#.
        ##..#.....
        #...##..#.
        ####.#...#
        ##.##.###.
        ##...#.###
        .#.#.#..##
        ..#....#..
        ###...#.#.
        ..###..###

        Tile 1951:
        #.##...##.
        #.####...#
        .....#..##
        #...######
        .##.#....#
        .###.#####
        ###.##.##.
        .###....#.
        ..#.#..#.#
        #...##.#..

        Tile 1171:
        ####...##.
        #..##.#..#
        ##.#..#.#.
        .###.####.
        ..###.####
        .##....##.
        .#...####.
        #.##.####.
        ####..#...
        .....##...

        Tile 1427:
        ###.##.#..
        .#..#.##..
        .#.##.#..#
        #.#.#.##.#
        ....#...##
        ...##..##.
        ...#.#####
        .#.####.#.
        ..#..###.#
        ..##.#..#.

        Tile 1489:
        ##.#.#....
        ..##...#..
        .##..##...
        ..#...#...
        #####...#.
        #..#.#.#.#
        ...#.#.#..
        ##.#...##.
        ..##.##.##
        ###.##.#..

        Tile 2473:
        #....####.
        #..#.##...
        #.##..#...
        ######.#.#
        .#...#.#.#
        .#########
        .###.#..#.
        ########.#
        ##...##.#.
        ..###.#.#.

        Tile 2971:
        ..#.#....#
        #...###...
        #.#.###...
        ##.##..#..
        .#####..##
        .#..####.#
        #..#.#..#.
        ..####.###
        ..#.#.###.
        ...#.#.#.#

        Tile 2729:
        ...#.#.#.#
        ####.#....
        ..#.#.....
        ....#..#.#
        .##..##.#.
        .#.####...
        ####.#.#..
        ##.####...
        ##..#.##..
        #.##...##.

        Tile 3079:
        #.#.#####.
        .#..######
        ..#.......
        ######....
        ####.#..#.
        .#...#.##.
        #.#####.##
        ..#.###...
        ..#.......
        ..#.###...
    """.trimIndent()
}
private typealias Image = List<Pair<Int, Int>>
private fun Image.rotate(max: Int = 10) = map { (x,y) -> (max-1-y) to x }
private fun Image.flip(axis: Tile.Direction, max: Int= 10) = when(axis) {
    E,W -> map { (x,y) -> (max-1-x) to y }
    N,S -> map { (x,y) -> x to (max-1-y) }
}






