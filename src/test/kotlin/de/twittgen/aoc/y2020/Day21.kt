package de.twittgen.aoc.y2020

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.groups
import de.twittgen.aoc.util.mapLines


class Day21 : Day<List<Dish>>() {
    private val dishRegex = "(.+?)(?:\\(contains (.+)\\))?".toRegex()
    override fun String.parse() = mapLines { food ->
        dishRegex.groups(food)!!.let { (rawI, rawA) ->
            val ingredients = rawI.split(" ").filter { it.isNotEmpty() }
            val allergens = rawA.replace(" ","").split(",").filter { it.isNotEmpty() }
            ingredients to allergens
        }
    }

    init {
        part1(5, 2635) { dishes ->
            val possibleMeanings = getPossibleAllergenMeanings(dishes)
            val translatableIngredients = possibleMeanings.values.flatten()
            dishes.flatMap { it.first }.filterNot { it in translatableIngredients}.size
        }
        part2("mxmxvkd,sqjhc,fvjkl", "xncgqbcp,frkmp,qhqs,qnhjhn,dhsnxr,rzrktx,ntflq,lgnhmx") { dishes ->
            val possibleMeanings = getPossibleAllergenMeanings(dishes)
            val translated = resolve(possibleMeanings)
            translated.toList().sortedBy { it.first }.joinToString(",") { it.second }
        }
    }

    private fun resolve(possibleMeanings: Map<String, List<String>>): Map<String, String> {
        val resolved = possibleMeanings.filterValues { it.size ==1 }.mapValues { (_,v) -> v.first() }.toMutableMap()
        var remaining = possibleMeanings.filterKeys { it !in resolved.keys }
        while(remaining.isNotEmpty()) {
            remaining = remaining.mapValues { (_,v) -> v.filterNot { it in resolved.values } }
            resolved += remaining.filterValues { it.size == 1 }.mapValues { (_, v) -> v.first() }
            remaining = remaining.filterKeys { it !in resolved.keys }
        }
        return resolved
    }

    private fun getPossibleAllergenMeanings(dishes: List<Dish>): Map<String, List<String>> {
        val possibleMeanings = mutableMapOf<String, List<String>>()
        dishes.forEach { dish -> dish.second.forEach { allergen ->
            val oldPossible = possibleMeanings[allergen]
            val newPossible = oldPossible?.let { dish.first.filter { it in oldPossible } } ?: dish.first
            possibleMeanings[allergen] = newPossible
        } }
        return possibleMeanings
    }

    override val example = """
        mxmxvkd kfcds sqjhc nhms (contains dairy, fish)
        trh fvjkl sbzzf mxmxvkd (contains dairy)
        sqjhc fvjkl (contains soy)
        sqjhc mxmxvkd sbzzf (contains fish)
    """.trimIndent()
}
private typealias Dish = Pair<List<String>, List<String>>