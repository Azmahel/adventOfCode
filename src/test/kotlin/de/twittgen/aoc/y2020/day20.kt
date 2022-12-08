package de.twittgen.aoc.y2020

import de.twittgen.aoc.util.FileUtil
import de.twittgen.aoc.y2020.day20.Tile
import de.twittgen.aoc.y2020.day20.Tile.Direction.*
import org.junit.jupiter.api.Test
import kotlin.math.roundToInt
import kotlin.math.sqrt

class day20 {
    val input = FileUtil.readInput("2020/day20")
    val example = """
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

    fun parseInput(s: String): List<Tile> {
        return s.replace("\r","")
            .split("\n\n")
            .map { parseTile(it) }
    }
    val tileNumber = Regex("Tile (\\d+):")
    private fun parseTile(s: String): Tile {
        val (id) = tileNumber.matchEntire(s.lines().first())!!.destructured
        val body = s.lines().drop(1)
       return Tile(
           id.toInt(),
           Borders(
               body.first().toEdgeNumber(),
               body.map { it.last() }.joinToString("").toEdgeNumber(),
               body.last().toEdgeNumber(),
               body.map { it.first() }.joinToString("").toEdgeNumber()
           ),
           body.drop(1).dropLast(1).mapIndexed { x, it ->
               it.drop(1).dropLast(1).mapIndexedNotNull { y, it ->
                   if(it == '#') x to y else null
               }
           }.flatten()
       )
    }

    data class Tile(
        val id : Int,
        val borders : Borders,
        val body: List<Pair<Int,Int>>,
        val size: Int = 10
        ) {
        fun fits(other: Tile): Boolean {
            return borders.all.any{ b -> other.fits(b) }
        }
        fun fits(other: Int): Boolean {
            return borders.all.any { b-> b == other || b == other.flipped(size) } }

        fun transformToFit(key: Int, from: Direction): Tile {
            var current = this
            var lock = current.getBorder(from)
            while(lock != key && lock != key.flipped(size)) {
                current = current.rotate()
                lock = current.getBorder(from)
            }
            if(lock != key) current = current.flip(from)
            return current
        }

        private fun getBorder(direction: Direction): Int {
            return when(direction) {
                N -> borders.n
                E -> borders.e
                S -> borders.s
                W -> borders.w
            }
        }

        fun rotate(): Tile {
            return this.copy(
                borders = with(borders) { Borders(e,s.flipped(size),w,n.flipped(size)) },
                body = body.rotate(size-2)
            )
        }

        fun flip(axis: Direction): Tile {
            val newBorders = when(axis) {
                N,S -> with(borders){Borders(n.flipped(size),w,s.flipped(size),e) }
                E,W -> with(borders){Borders(s,e.flipped(size),n,w.flipped(size)) }
            }
            return copy(
                borders = newBorders,
                body = body.flip(axis, size-2)
            )
        }



        enum class Direction { N,E,S,W }
    }

    data class Borders(
        val n: Int,
        val e: Int,
        val s: Int,
        val w: Int
    ) {
        val all = listOf(n,e,s,w)
    }

    @Test
    fun example() {
        val tiles = parseInput(example)
        val matching = tiles.map {
            it to (tiles - it).filter { other ->
                it.fits(other)
            }
        }
        val cornerCandidates = matching.filter { (_, matching ) -> matching.size ==2 }
        assert(
            cornerCandidates.fold(1L) { acc,x ->
                x.first.id.toLong() * acc
            } == 20899048083289
        )
    }

    @Test
    fun part1() {
        val tiles = parseInput(input)
        val matching = tiles.map {
            it to (tiles - it).filter { other ->
                it.fits(other)
            }
        }
        val cornerCandidates = matching.filter {
                (_, matching ) -> matching.size ==2
        }
        println(
            cornerCandidates.fold(1L) { acc,x ->
                x.first.id.toLong() * acc
            }
        )
    }

    @Test
    fun part2() {
        val tiles = parseInput(input)
        val matching = tiles.map {
            it to (tiles - it).filter { other ->
                it.fits(other)
            }
        }
        val result = assemble(matching)
        val image = result.join()
        val seaMonsters = countMonster(image)
        println(image.count() - (seaMonsters* seamonster.count()))
    }

    private fun countMonster(image: List<Pair<Int, Int>>): Int {
        val rot : List<Pair<Int, Int>>.() -> List<Pair<Int, Int>> = { rotate(maxByOrNull { it.first }!!.first) }
        val flipH: List<Pair<Int, Int>>.() -> List<Pair<Int, Int>> = { flip(E,maxByOrNull { it.first }!!.first) }
        val flipV: List<Pair<Int, Int>>.() -> List<Pair<Int, Int>> = { flip(N,maxByOrNull { it.first }!!.first) }
        val possibleImages = listOf(
            image,
            image.rot(),
            image.rot().rot(),
            image.rot().rot().rot(),
            image.flipH().rot(),
            image.flipH().rot().rot(),
            image.flipH().rot().rot().rot(),
            image.flipV().rot(),
            image.flipV().rot().rot(),
            image.flipV().rot().rot().rot(),
            image.flipH().flipV().rot(),
            image.flipH().flipV().rot().rot(),
            image.flipH().flipV().rot().rot().rot()
        ).distinct()
        val counts = possibleImages.map {
            it.countMonsters()
        }
        return counts.maxOrNull()!!
    }

    val seamonster= """
                  # 
#    ##    ##    ###
 #  #  #  #  #  #   
    """.trimIndent().lines().mapIndexed { x, s ->
        s.mapIndexedNotNull { y, it -> if(it == '#') x to y else null }
    }.flatten()

    fun List<Pair<Int,Int>>.countMonsters(): Int {

        val max = maxByOrNull { it.first }!!.first
        return (0..max).flatMap { x->
            (0..max).map { y ->
               val isSeaMonster = seamonster.all { (sx,sy) -> firstOrNull { it == (x+sx) to (y+sy) } != null }
                isSeaMonster
            }
        }.count { it}.also {
            if (it > 0) print()
        }
    }

    fun List<Pair<Int,Int>>.print() {
        val max = maxByOrNull { it.first }!!.first
        (0..max).forEach { x ->
            (0..max).forEach { y ->
                print(if(x to y in this) "#" else ".")
            }
            println()
        }
    }

    fun Map<Pair<Int,Int>,Tile>.join(): List<Pair<Int, Int>> {
        val max = keys.maxByOrNull { it.first }!!.first
        return (0..max).flatMap { x->
            (0..max).flatMap { y->
                with(get(x to y)!!) {
                    body.map { (ix, iy) ->
                        (ix + (x * (size-2))) to (iy + (y * (size-2)))
                    }
                }
            }
        }
    }

    fun assemble(matching: List<Pair<Tile, List<Tile>>>): Map<Pair<Int, Int>, Tile> {

        val start = matching.first { it.second.size == 2 }.rotateToFitTopCorner()

        val result = mutableMapOf((0 to 0) to start)
        val dimension = sqrt(matching.size.toDouble()).roundToInt()
        (0 until dimension).forEach { x ->
            (0 until dimension).forEach { y ->
                if(x != 0 || y != 0){
                    if(y !=0 ) {
                        val last = result[x to y-1]!!
                        val options = matching.first { it.first.id == last.id }.second
                        val next = options.first{ it.fits(last.borders.e) }.transformToFit(last.borders.e, W)
                        result[x to y ] = next
                    } else {
                        val last = result[x-1 to y]!!
                        val options = matching.first { it.first.id == last.id }.second
                        val next = options.first{ it.fits(last.borders.s) }.transformToFit(last.borders.s, N)
                        result[x to y ] = next
                    }
                }
            }
        }
        return result
    }
}



private fun  Pair<Tile, List<Tile>>.rotateToFitTopCorner(): Tile {
    var current = first
    while( second.none { it.fits(current.borders.e) } || second.none { it.fits(current.borders.s)} ) {
        if(second.none { it.fits(current.borders.w) } || second.none { it.fits(current.borders.s)}) {
            current = current.flip(E)
        } else {
            current = current.rotate()
        }
    }
    return current
}
fun List<Pair<Int,Int>>.rotate(max: Int = 10) = map { (x,y) ->
    (max-1-y) to x
}

fun List<Pair<Int,Int>>.flip(axis: Tile.Direction, max: Int= 10) = when(axis) {
    E,W -> map { (x,y) ->
        (max-1-x) to y
    }
    N,S -> map { (x,y) ->
        x to (max-1-y)
    }
}
fun Int.flipped(size: Int = 10) = toString(2).padStart(size,'0').reversed().toInt(2)
fun String.toEdgeNumber(): Int {
    val x = map { if (it == '#') 1 else 0 }.joinToString("")
    return x.toInt(2)
}




