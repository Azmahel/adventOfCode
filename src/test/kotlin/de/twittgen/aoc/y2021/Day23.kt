package de.twittgen.aoc.y2021

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.replaceAt
import de.twittgen.aoc.y2021.Day23.Amphipod.*
import java.lang.IllegalStateException
import kotlin.math.abs

class Day23 : Day<List<Day23.Space>>() {

    override fun String.parse() : List<Space> {
        val hallway = emptyHallway.toMutableList()
        lines()
            .reversed()
            .drop(1)
            .take(2)
            .map { it.filterNot { it == '#' }.filterNot { it == ' ' } }
            .forEach {
                hallway[2] = hallway[2].enter(it[0].toAmphipod()).first
                hallway[4] = hallway[4].enter(it[1].toAmphipod()).first
                hallway[6] = hallway[6].enter(it[2].toAmphipod()).first
                hallway[8] = hallway[8].enter(it[3].toAmphipod()).first
            }
        return hallway
    }

    init {
        part1(12521,14510) {
            it.findShortestShuffle(sorted)
        }
        part2(44169, 49180) {
            it.extend().findShortestShuffle(sortedDeep)
        }
    }

    private val extension = """
        DCBA
        DBAC
    """.trimIndent()

    private fun List<Space>.extend() : List<Space> {
        val result = emptyHallway.toMutableList()
        result[2] = DeepHome(Amber).enter((this[2] as Home).secondSpot!!).first
        result[4] =  DeepHome(Bronze).enter((this[4] as Home).secondSpot!!).first
        result[6] =  DeepHome(Copper).enter((this[6] as Home).secondSpot!!).first
        result[8] =  DeepHome(Desert).enter((this[8] as Home).secondSpot!!).first
        extension.lines().reversed().forEach {
            result[2] = result[2].enter(it[0].toAmphipod()).first
            result[4] = result[4].enter(it[1].toAmphipod()).first
            result[6] = result[6].enter(it[2].toAmphipod()).first
            result[8] = result[8].enter(it[3].toAmphipod()).first
        }
        result[2] = result[2].enter((this[2] as Home).firstSpot!!).first
        result[4] = result[4].enter((this[4] as Home).firstSpot!!).first
        result[6] = result[6].enter((this[6] as Home).firstSpot!!).first
        result[8] = result[8].enter((this[8] as Home).firstSpot!!).first
        return result
    }

    private fun List<Space>.findShortestShuffle(sorted: List<Space>): Int {
        return findShortestShuffle(mapOf(this to (0 to false), sorted to (Int.MAX_VALUE to true )), sorted)
    }


    private fun findShortestShuffle( state : Map<List<Space>, Pair<Int, Boolean>>, sorted: List<Space>) : Int {
        val explorableStates = state
            .entries
            .filter { it.value.first < state[sorted]!!.first }
            .filterNot { it.value.second }
            .sortedBy { it.value.first }

        if(explorableStates.isEmpty()) return state[sorted]!!.first
        val nextStates = explorableStates.asSequence().flatMap { current ->
            current.key
                .toList()
                .filter { space -> space.canLeave().first }
                .flatMap { space ->
                    val (_, delta1) = space.canLeave()
                    val index = current.key.toList().indexOf(space)
                    (current.key.toList().drop(index + 1).takeWhile { it.canPassThrough() } + current.key.toList()
                        .take(index).takeLastWhile { it.canPassThrough() })
                        .filter { it.canMoveToHere(space) }
                        .map { other ->
                            val next = current.key.toList().toMutableList().replaceAt(index, space.leave())
                            val (newSpace, delta2) = other.enter(space.occupant!!)
                            val otherIndex = current.key.toList().indexOf(other)
                            next.replaceAt(otherIndex, newSpace)
                            val cost = ((abs(index - otherIndex)) + delta1 + delta2) * space.occupant!!.cost
                            next to (current.value.first + cost to false)
                        }
                }
        }.filter { (state[it.first]?.first ?: Int.MAX_VALUE) > it.second.first }
            .filter { it.second.first < state[sorted]!!.first }
            .sortedByDescending { it.second.first }
            .associate { it.first to it.second }
        if (nextStates.isEmpty()) return state[sorted]!!.first
        val next = explorableStates.associate { (k, v) -> k to (v.first to true) }.plus(sorted to state[sorted]!!).plus(nextStates).toMutableMap()
        next[sorted] = next[sorted]!!.first to true
        return findShortestShuffle(next, sorted)
    }


    private fun Char.toAmphipod() = when(this) {
        'A' -> Amber
        'B' -> Bronze
        'C' -> Copper
        'D' -> Desert
        else -> throw IllegalStateException()
    }

    private val emptyHallway = listOf(
        Hallway(0),
        Hallway(1),
        Home(Amber) ,
        Hallway(2),
        Home(Bronze),
        Hallway(3),
        Home(Copper),
        Hallway(4),
        Home(Desert),
        Hallway(5),
        Hallway(6)
    )

    private val sorted = listOf(
        Hallway(0),
        Hallway(1),
        Home(Amber, Amber, Amber) ,
        Hallway(2),
        Home(Bronze, Bronze, Bronze),
        Hallway(3),
        Home(Copper, Copper, Copper),
        Hallway(4),
        Home(Desert, Desert, Desert),
        Hallway(5),
        Hallway(6)
    )

    private val sortedDeep = listOf(
        Hallway(0),
        Hallway(1),
        DeepHome(Amber, Amber, Amber, Amber, Amber) ,
        Hallway(2),
        DeepHome(Bronze, Bronze, Bronze, Bronze, Bronze),
        Hallway(3),
        DeepHome(Copper, Copper, Copper, Copper, Copper),
        Hallway(4),
        DeepHome(Desert, Desert, Desert, Desert, Desert),
        Hallway(5),
        Hallway(6)
    )


    interface Space {
       val occupant: Amphipod?
       fun canMoveToHere(other: Space) : Boolean
       fun canPassThrough() : Boolean
       fun enter(a: Amphipod): Pair<Space, Int>
       fun canLeave(): Pair<Boolean, Int>
       fun leave(): Space
    }

    //TODO model using stack
    data class Hallway(val id : Int, override val occupant: Amphipod? = null) : Space {

        override fun canMoveToHere(other: Space) = other !is Hallway && occupant == null
        override fun canPassThrough() = occupant == null
        override fun enter(a: Amphipod) = copy(occupant = a) to 0
        override fun canLeave() = (occupant != null) to 0
        override fun leave() = copy(occupant = null)
    }

    data class Home(
        val type: Amphipod?,
        val firstSpot: Amphipod? = null,
        val secondSpot: Amphipod? = null
    ): Space {
        override val occupant: Amphipod? get() = firstSpot ?: secondSpot
        override fun canMoveToHere(other: Space) = other.occupant == type && firstSpot == null && secondSpot?.let { it == type } ?: true
        override fun canPassThrough() = true
        override fun enter(a: Amphipod) = secondSpot?.let { this.copy(firstSpot = a) to 1 } ?: (this.copy(secondSpot = a) to 2)
        override fun leave() = firstSpot?.let { copy(firstSpot = null) } ?: copy(secondSpot = null)
        fun happy() = copy(firstSpot = type, secondSpot = type)
        override fun canLeave() = if ((firstSpot ?: type) == type && (secondSpot ?: type) == type)
            false to 0
        else
            firstSpot?.let { true to 1 } ?: secondSpot?.let { true to 2 } ?: (false to 0)
    }

    data class DeepHome(
        val type: Amphipod?,
        private val firstSpot: Amphipod? = null,
        private val secondSpot: Amphipod? = null,
        private val thirdSpot: Amphipod? = null,
        private val fourthSpot: Amphipod? = null
    ): Space {
        override val occupant: Amphipod? get() = firstSpot ?: secondSpot ?: thirdSpot ?: fourthSpot
        override fun canMoveToHere(other: Space) = other.occupant == type &&
                firstSpot == null &&
                secondSpot?.let { it == type } ?: true &&
                thirdSpot?.let { it == type } ?: true &&
                fourthSpot?.let { it == type } ?: true
        override fun canPassThrough() = true
        override fun enter(a: Amphipod) =
            secondSpot?.let { this.copy(firstSpot = a) to 1 } ?:
            thirdSpot?.let { this.copy(secondSpot = a) to 2 } ?:
            fourthSpot?.let { this.copy(thirdSpot = a) to 3 } ?:
            (this.copy(fourthSpot = a) to 4)
        override fun leave() =
            firstSpot?.let { copy(firstSpot = null) } ?:
            secondSpot?.let { copy(secondSpot = null) } ?:
            thirdSpot?.let { copy(thirdSpot = null) } ?:
            copy(fourthSpot = null)
        override fun canLeave() = if (
            (firstSpot ?: type) == type &&
            (secondSpot ?: type) == type &&
            (thirdSpot ?: type) == type &&
            (fourthSpot ?: type) == type
        )
            false to 0
        else
            firstSpot?.let { true to 1 } ?:
            secondSpot?.let { true to 2 } ?:
            thirdSpot?.let { true to 3 } ?:
            fourthSpot?.let { true to 4 } ?:
            (false to 0)
    }

    enum class Amphipod( val cost: Int) {
        Amber(1), Bronze(10), Copper(100), Desert(1000)
    }

    override val example = """
        #############
        #...........#
        ###B#C#B#D###
          #A#D#C#A#
          #########    
    """.trimIndent()
}