package dev.mfazio.pennydrop.types

import dev.mfazio.pennydrop.game.AI

data class Player(
    var playerName: String = "",
    var isHuman: Boolean = true,
    var selectedAI: AI? = null
) {
    var pennies: Int = defaultPennyCount

    fun addPennies(count: Int = 1) {
        pennies += count
    }
    
    var isRolling: Boolean = false

    companion object {
        const val defaultPennyCount = 10
    }
}
