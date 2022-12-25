package de.twittgen.aoc

import de.twittgen.aoc.util.getIdentifier
import org.junit.jupiter.api.DisplayNameGenerator

class DayNameGenerator : DisplayNameGenerator.Standard() {
    override fun generateDisplayNameForClass(testClass: Class<*>?) = testClass!!.getIdentifier()
}