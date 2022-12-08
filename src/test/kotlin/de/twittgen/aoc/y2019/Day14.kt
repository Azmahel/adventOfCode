package de.twittgen.aoc.y2019

import de.twittgen.aoc.util.FileUtil
import org.junit.jupiter.api.Test

class Day14 {
    val input = FileUtil.readInput("2019/day14").parseInput()

    @Test
    fun getA() = println(getOreNeeded())

 @Test
 fun getB() {
     val oreNeeded = 1_000_000_000_000L
    val maxOrePerFuel = getOreNeeded()!!
     var fuelProduced = oreNeeded / maxOrePerFuel
     var requirements = mapOf("FUEL" to (oreNeeded / maxOrePerFuel)).breakDown(input)
        while(true) {
            val fuelrequested = ((oreNeeded - requirements["ORE"]!!)/ maxOrePerFuel).coerceAtLeast(1)
            val newReq = (mapOf("FUEL" to fuelrequested ) + requirements.filter{ it.key !="FUEL"}).breakDown(input)
            if (newReq["ORE"]!! >= oreNeeded) break
            requirements = newReq
            println("${requirements["ORE"]!! / (oreNeeded /100)} % done")
            fuelProduced +=fuelrequested
        }
     println(requirements)
     println("FUEL: $fuelProduced")
 }

    fun getOreNeeded() =mapOf("FUEL" to 1L).breakDown(input)["ORE"]
    fun Map<String,Long>.breakDown(reactions: Map<String, Reaction>): Map<String, Long> {
        val requirements = this.toMutableMap()
        while(requirements.any { it.key != "ORE" && it.value > 0 }) {
            requirements.filter {  it.key != "ORE" && it.value > 0 }.keys.forEach {
                val reaction = reactions[it]!!
                val timesApplicable = (requirements[it]!! / reaction.result.ammount ).coerceAtLeast(1)
                requirements[it] = requirements[it]!! - reaction.result.ammount*timesApplicable
                reaction.requirements.forEach {
                    requirements.put(it.name, (requirements[it.name] ?:0) + it.ammount*timesApplicable )
                }
            }
            //println(requirements)
        }
        return requirements.toMap()
    }
    data class Reaction(
        val result : ReactionComponent,
        val requirements: List<ReactionComponent>

    )
    fun String.toReaction() = split(" => ").let{
        Reaction(
            it[1].toReactionComponent(),
            it[0].split(",").map { it.toReactionComponent() }
        )
    }
    data class ReactionComponent(
        val name: String,
        val ammount: Int
    )
    fun String.toReactionComponent() = trim().split(" ").let{
        ReactionComponent(it[1], it[0].toInt())
    }
    private fun String.parseInput() = replace("\r","")
        .split("\n")
        .map { it.toReaction() }
        .map { it.result.name to it}
        .toMap()

}