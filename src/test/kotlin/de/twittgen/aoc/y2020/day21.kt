package de.twittgen.aoc.y2020

import de.twittgen.aoc.y2019.shared.util.FileUtil
import org.junit.jupiter.api.Test
typealias Dish = Pair<List<String>, List<String>>
class day21 {
    val input = FileUtil.readInput("2020/day21")
    val example = """
        mxmxvkd kfcds sqjhc nhms (contains dairy, fish)
        trh fvjkl sbzzf mxmxvkd (contains dairy)
        sqjhc fvjkl (contains soy)
        sqjhc mxmxvkd sbzzf (contains fish)
    """.trimIndent()

    @Test
    fun example() {
        val dishes = parseInput(example)
        val possibleMeanings = getPossibleAllergenMeanings(dishes)
        val translatableIngredients = possibleMeanings.values.flatten()
        val impossibleIngredients = dishes.flatMap { it.first }.filterNot { it in translatableIngredients}
        assert(
            impossibleIngredients.size == 5
        )
    }

    @Test
    fun part1() {
        val dishes = parseInput(input)
        val possibleMeanings = getPossibleAllergenMeanings(dishes)
        val translatableIngredients = possibleMeanings.values.flatten()
        val impossibleIngredients = dishes.flatMap { it.first }.filterNot { it in translatableIngredients}
        println(
            impossibleIngredients.size
        )
    }

    @Test
    fun part2() {
        val dishes = parseInput(input)
        val possibleMeanings = getPossibleAllergenMeanings(dishes)
        val translated = resolve(possibleMeanings)
        val result = translated.toList().sortedBy { it.first }.map { it.second }
        println(result.joinToString(","))
    }

    private fun resolve(possibleMeanings: Map<String, List<String>>): Map<String, String> {
            val resolved = possibleMeanings.filterValues { it.size ==1 }.mapValues { (_,v) -> v.first() }.toMutableMap()
            var remaining = possibleMeanings.filterKeys { it !in resolved.keys }
            while(remaining.isNotEmpty()) {
                remaining = remaining.mapValues { (_,v) ->
                    v.filterNot { it in resolved.values }
                }
                resolved += remaining.filterValues { it.size == 1 }.mapValues { (_, v) -> v.first() }
                remaining = remaining.filterKeys { it !in resolved.keys }
            }
        return resolved
    }

    val dishRegex = "(.+?)(?:\\(contains (.+)\\))?".toRegex()
    private fun parseInput(s: String): List<Dish> {
        return s.lines().map {
            val (rawI, rawA) = dishRegex.matchEntire(it)!!.destructured
            val ingredients = rawI.split(" ").filter { it.isNotEmpty() }
            val allergens = rawA.replace(" ","").split(",").filter { it.isNotEmpty() }
            ingredients to allergens
        }
    }

    fun getPossibleAllergenMeanings(dishes: List<Dish>): Map<String, List<String>> {
        val possibleMeanings = mutableMapOf<String, List<String>>()
        dishes.forEach { dish ->
            dish.second.forEach { allergen ->
                val oldPossible = possibleMeanings[allergen]
                val newPossible = oldPossible?.let { dish.first.filter { it in oldPossible } } ?: dish.first
                possibleMeanings[allergen] = newPossible
            }
        }
        return possibleMeanings
    }
}