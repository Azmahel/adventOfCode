package de.twittgen.aoc.y2022

import de.twittgen.aoc.Day
import de.twittgen.aoc.y2022.Day16.Valve
import de.twittgen.aoc.util.filterMirrors
import de.twittgen.aoc.util.groups
import de.twittgen.aoc.util.toPairOfLists
import kotlinx.coroutines.*


class Day16 : Day<CaveMap>() {
    private val pattern = Regex("Valve ([A-Z]{2}) has flow rate=([0-9]+); tunnel[s]? lead[s]? to valve[s]? (.+)")

    override fun String.parse() = lines().map{l -> pattern.groups(l)!!.let { (valve,flow,other)->
        Valve(valve, flow.toInt()) to other.split(", ").map { valve to it }
    } }.toPairOfLists().let { p ->
        val valveMap = p.first.associateBy { it.name }
        p.first to p.second.flatten().groupBy({valveMap[it.first]!!},{valveMap[it.second]!! })
    }

    init {
        part1(1651,1947) { it.toShortestPaths().findMax(it.start(), 30) }
        part2(1707,2556,) { runBlocking(Dispatchers.Default) {
            it.toShortestPaths().shareWorkOptions().map { (p, e) ->
                async { p.findMax(it.start(), 26)  }  to async{ e.findMax(it.start(), 26)}
            }.maxOf { (a,b) -> a.await() + b.await() }
        } }
    }

    private fun CaveMap.start() = first.find { it.isStart() }!!
    private fun CaveMap.toShortestPaths()= first.filter{ it.isRelevant() }.associateWith { it.getShortestPaths(second).toMap() }
    private fun Distances.trim() = mapValues { (_,v) -> v.filter { it.key in keys } }

    private fun Distances.shareWorkOptions(): List<Pair<Distances, Distances>>{
        val start = entries.find { it.key.isStart() }!!.toPair()
        val options = (this - start.first).toList().calculatePossibleShares().filterMirrors()
        return options.map { (it.first + start).trim() to (it.second + start).trim()  }
    }

    private fun List<Pair<Valve,Map<Valve, Int>>>.calculatePossibleShares(
        current: Pair<Distances, Distances> = emptyMap<Valve,Map<Valve,Int>>() to emptyMap()
    ): List<Pair<Distances, Distances>> {
        if(isEmpty()) return listOf(current)
        val a =  drop(1).calculatePossibleShares((current.first + first()) to current.second)
        val b =  drop(1).calculatePossibleShares((current.first) to current.second + first())
        return (a + b)
    }

    private fun Distances.findMax(current: Valve, time: Int = 30, open: MutableSet<Valve> = mutableSetOf(), flow: Int = 0): Int =
        get(current)!!.getRelevant(open, time).maxOfOrNull { (v, d) ->
            open += v
            findMax(v, time - d - 1, open, flow + (v.flow * (time - d - 1))).also { open -=v }
        } ?: flow

    data class Valve(val name: String, val flow: Int) {
        fun getShortestPaths(adj : Adjacency) : List<Distance> {
            val found = mutableMapOf(this to 0)
            var dist = 0
            while (found.size < adj.keys.size) {
                found.filter { it.value == dist }.forEach { (c, _) ->
                    adj[c]!!.forEach { n -> if(found.getOrDefault(n, Int.MAX_VALUE) > dist+1) found[n] = dist+1 }
                }
                dist ++
            }
            return found.filter { (k,_) -> k.isRelevant() }.map { (k,v) -> k to v }
        }
        fun isStart() = name == "AA"
        fun isRelevant() = isStart() || flow != 0
    }

    private fun Map<Valve,Int>.getRelevant(open: Set<Valve>, time: Int) =
        filter { it.value <= time -1 && it.key !in open  && it.key.flow != 0 }

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

typealias Distances = Map<Valve, Map<Valve,Int>>
typealias Distance = Pair<Valve,Int>
typealias Adjacency = Map<Valve,List<Valve>>
typealias CaveMap = Pair<List<Valve>, Adjacency>
