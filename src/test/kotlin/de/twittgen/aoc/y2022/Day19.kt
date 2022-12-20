package de.twittgen.aoc.y2022

import de.twittgen.aoc.Day
import de.twittgen.aoc.Day.TestType.SLOW
import de.twittgen.aoc.y2022.Day19.Blueprint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class Day19: Day<List<Blueprint>>() {
    private val bpPattern = Regex("\\D*(\\d+)\\D*(\\d+)\\D*(\\d+)\\D*(\\d+)\\D*(\\d+)\\D*(\\d+)\\D*(\\d+)\\D*")
    override fun String.parse() = lines().map { bpPattern.matchEntire(it)!!.destructured.let { (id,ore,clay,obs1,obs2,geo1,geo2) ->
        Blueprint(
            id.toInt(),
            listOf(
            Robot(listOf(ore.toInt(),0,0,0), listOf(1,0,0,0)),
            Robot(listOf(clay.toInt(),0,0,0), listOf(0,1,0,0)),
            Robot(listOf(obs1.toInt(), obs2.toInt(),0,0), listOf(0,0,1,0)),
            Robot(listOf(geo1.toInt(),0, geo2.toInt(), 0), listOf(0,0,0,1))
            )
        )
    } }

    init {
        part1(33, 851) { runBlocking(Dispatchers.Default) {  it
            .map { bp -> bp.id to async {bp.findMax()} }
            .sumOf { (id,v) -> id * v.await() }
        }}
        part2(null,12160, SLOW ) { runBlocking(Dispatchers.Default) { //SLOW Example is disabled, expected should be 62*56
            it.take(3).map {  bp -> async {  bp.findMax(State(32))}}.fold(1) {i,v -> i* v.await() }
        } }

    }
    private val geode = 3

    private fun Blueprint.findMax(state: State = State()) : Int {

        val availableRobots = robots.filter {r ->
            r.cost.withIndex().filter { it.value !=0 }.all { (resource, _) -> state.production[resource] != 0 }
        }.filter { r ->
           r.production.filterIndexed { i, v -> v!=0 && (i == geode || state.production[i] < max[i]) }.isNotEmpty()
        }

        return availableRobots.maxOf { robot ->
            var time =state.time
            val resources = state.resources.toMutableList()
            val production = state.production.toMutableList()
            while(resources.mapIndexed { i, v -> v- robot.cost[i]}.any { it <0 }) {
                time --
                production.forEachIndexed {i, v -> resources[i] += v}
                if(time == 0)
                    return@maxOf resources[geode]
            }
            time --
            production.forEachIndexed {i, v -> resources[i] += v}
            if(time == 0)
                return@maxOf resources[geode]
            robot.cost.forEachIndexed { i, v ->  resources[i] -= v }
            robot.production.forEachIndexed { i, v -> production[i] += v  }
            findMax(State(time, resources, production))
        }
    }

    data class State(
        val time : Int = 24,
        val resources: List<Int> = listOf(0, 0, 0, 0),
        val production: List<Int> = listOf(1, 0, 0, 0)
    )

    data class Blueprint(val id: Int, val robots: List<Robot>) {
        val max = (0..3).map { i ->  robots.maxOf { it.cost[i] } }
    }
    data class Robot(val cost: List<Int>, val production: List<Int>)

    override val example = """
        Blueprint 1: Each ore robot costs 4 ore. Each clay robot costs 2 ore. Each obsidian robot costs 3 ore and 14 clay. Each geode robot costs 2 ore and 7 obsidian.
        Blueprint 2: Each ore robot costs 2 ore. Each clay robot costs 3 ore. Each obsidian robot costs 3 ore and 8 clay. Each geode robot costs 3 ore and 12 obsidian.
    """.trimIndent()
}