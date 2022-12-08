package de.twittgen.aoc.y2019.day1

typealias  Module = Int
data class Rocket(val modules : List<Module>) {
    fun getFuelNeeded() = modules.sumBy { it.getFuelNeeded() }
    fun getFuelNeededRecursive() = modules.sumBy { it.getFuelNeededRecursive() }

    private fun Module.getFuelNeeded() =  this / 3 -2
    private fun Module.getFuelNeededRecursive() : Int {
        val fuelNeeded = getFuelNeeded()
        if(fuelNeeded <=0) return 0
        return fuelNeeded + fuelNeeded.getFuelNeededRecursive()
    }
}