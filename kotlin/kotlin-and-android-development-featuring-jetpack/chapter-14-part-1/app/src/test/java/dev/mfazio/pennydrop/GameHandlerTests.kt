package dev.mfazio.pennydrop

import dev.mfazio.pennydrop.game.AI
import dev.mfazio.pennydrop.game.GameHandler
import dev.mfazio.pennydrop.game.TurnEnd
import dev.mfazio.pennydrop.types.Player
import org.junit.Assert.*
import org.junit.BeforeClass
import org.junit.Test

class GameHandlerTests {

    @Test
    fun `Test nextPlayer() via pass() function`() {
        val currentPlayer = testPlayers.first { it.playerName == "Emily" }
        val nextPlayer = testPlayers.first { it.playerName == "Hazel" }

        checkNextPlayer(currentPlayer, nextPlayer)
    }

    @Test
    fun `Test last nextPlayer() via pass() function`() {
        val currentPlayer = testPlayers.first { it.playerName == "Riverboat Ron" }
        val nextPlayer = testPlayers.first { it.playerName == "Michael" }

        checkNextPlayer(currentPlayer, nextPlayer)
    }

    private fun checkNextPlayer(currentPlayer: Player, nextPlayer: Player) =
        GameHandler.pass(testPlayers, currentPlayer).also { result ->
            assertTrue(result.playerChanged)
            assertEquals(TurnEnd.Pass, result.turnEnd)
            assertEquals(currentPlayer, result.previousPlayer)
            assertEquals(nextPlayer, result.currentPlayer)
        }

    companion object {
        private lateinit var testPlayers: List<Player>

        @BeforeClass
        @JvmStatic
        fun setUpTestPlayers() {
            this.testPlayers = listOf(
                Player("Michael", true),
                Player("Emily", true),
                Player("Hazel", true),
                Player("Riverboat Ron", false, selectedAI = AI.basicAI[5])
            )
        }
    }
}