package dev.mfazio.pennydrop.types

import dev.mfazio.pennydrop.game.AI

data class Player(
    var playerName: String = "",
    var isHuman: Boolean = true,
    var selectedAI: AI? = null
) {
    var pennies: Int = defaultPennyCount

    var isRolling: Boolean = false

    fun addPennies(count: Int = 1) {
        pennies += count
    }

    fun penniesLeft(subtractPenny: Boolean = false): Boolean =
        (pennies - (if(subtractPenny) 1 else 0)) > 0

    companion object {
        const val defaultPennyCount = 10
    }
}