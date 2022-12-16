package de.twittgen.aoc.current.y2022

import de.twittgen.aoc.current.Day
import de.twittgen.aoc.current.Day.TestType.SLOW
import de.twittgen.aoc.current.y2022.Day16.Valve
import de.twittgen.aoc.util.toPairOfLists
import java.lang.Math.abs


class Day16 : Day<CaveMap>() {

    override fun String.parse() = lines().map { l ->
        valveMatcher.matchEntire(l)!!.destructured.let { (valve, flow, other) ->
            Valve(valve, flow.toInt()) to other.split(", ").map { valve to it }
        }
    }.toPairOfLists().let { p ->
        val valveMap = p.first.associateBy { it.name }
        p.first to p.second.flatten().groupBy({valveMap[it.first]!!},{valveMap[it.second]!! })
    }

    private val valveMatcher =
        Regex("Valve ([A-Z]{2}) has flow rate=([0-9]+); tunnel[s]? lead[s]? to valve[s]? (.+)")

    init {
        part1(1651,) {
            it.toShortestPaths().findRelease(it.start(), 30)
        }
        part2(1707,null, SLOW) {
            it.toShortestPaths().run{ State(it.start()).findElephantRelease(this) }
        }
    }

    private fun CaveMap.start() = first.find { it.name == "AA" }!!

    private fun CaveMap.toShortestPaths()= first.associateWith { it.getShortestPaths(second) }

    private fun Distances.findRelease(
        current: Valve,
        time: Int = 30,
        open: Set<Valve> = emptySet(),
        flow: Int = 0
    ): Int = get(current)!!.getRelevant(open, time)
        .maxOfOrNull { (v,d) -> findRelease(v, time-d-1, open+v, flow + (v.flow * (time-d-1))) } ?: flow

    data class Valve(val name: String, val flow: Int) {
        fun getShortestPaths(adj : Adjacencies) : List<Distance> {
            val found = mutableMapOf(this to 0)
            var dist = 0
            while (found.size < adj.keys.size) {
                found.filter { it.value == dist }.forEach { (c, _) ->
                    adj[c]!!.forEach { n -> if(found.getOrDefault(n, Int.MAX_VALUE) > dist+1) found[n] = dist+1 }
                }
                dist ++
            }
            return found.map { (k,v) -> k to v }
        }
    }

    private fun List<Distance>.getRelevant(open: Set<Valve>, time: Int) = filter { it.second <= time -1 && it.first !in open  && it.first.flow != 0 }

    private fun State.findElephantRelease(dist: Distances) : Int {
        val nextOpen = open.toMutableSet()
        var nextRelease = released + open.sumOf { it.flow }
        var pD = personDelay
        var eD = elephantDelay
        var t = time
        if(personDelay > 0 && elephantDelay > 0) {
            val dT = minOf(personDelay, elephantDelay)
            pD -= dT
            eD -= dT
            t -= dT
            nextRelease += dT * open.sumOf { it.flow }
        }
        val nextPerson = if(pD == 0) {
            dist[person]!!.getRelevant(nextOpen,t).also { nextOpen += person }
        } else {
            listOf(person to pD-1)
        }
        val nextElephant = if(eD == 0) {
            dist[elephant]!!.getRelevant(nextOpen,t).also { nextOpen += elephant }
        } else {
            listOf(elephant to eD-1)
        }
        return nextPerson.flatMap { p -> nextElephant.map { e -> p to e } }.map { (p, e) ->
            State(p.first, p.second, e.first, e.second, nextOpen, t -1, nextRelease)
                .findElephantRelease(dist)
        }.maxOrNull() ?: (released + (open.sumOf { it.flow }*(time)))

    }

    data class State(
        val person: Valve,
        val personDelay: Int = 0,
        val elephant: Valve = person,
        val elephantDelay: Int = 0,
        val open: Set<Valve> = emptySet(),
        val time: Int = 27, //+1 because this will open AA on turn 1
        val released: Int = 0
    )

    override val example = """
        Valve AA has flow rate=0; tunnels lead to valves DD, II, BB
        Valve BB has flow rate=13; tunnels lead to valves CC, AA
        Valve CC has flow rate=2; tunnels lead to valves DD, BB
        Valve DD has flow rate=20; tunnels lead to valves CC, AA, EE
        Valve EE has flow rate=3; tunnels lead to valves FF, DD
        Valve FF has flow rate=0; tunnels lead to valves EE, GG
        Valve GG has flow rate=0; tunnels lead to valves FF, HH
        Valve HH has flow rate=22; tunnel leads to valve GG
        Valve II has flow rate=0; tunnels lead to valves AA, JJ
        Valve JJ has flow rate=21; tunnel leads to valve II
    """.trimIndent()
}

typealias Distances = Map<Valve, List<Distance>>
typealias Distance = Pair<Valve,Int>
typealias Adjacencies = Map<Valve,List<Valve>>
typealias CaveMap = Pair<List<Valve>, Adjacencies>
