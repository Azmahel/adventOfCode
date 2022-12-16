package de.twittgen.aoc.current

import de.twittgen.aoc.util.getIdentifier
import org.junit.jupiter.api.DisplayNameGenerator

class DayNameGenerator : DisplayNameGenerator.Standard() {
    override fun generateDisplayNameForClass(testClass: Class<*>?): String {
        return testClass!!.getIdentifier()
    }
}