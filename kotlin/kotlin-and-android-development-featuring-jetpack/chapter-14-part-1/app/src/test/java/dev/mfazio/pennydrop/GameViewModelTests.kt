package dev.mfazio.pennydrop

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import dev.mfazio.pennydrop.game.AI
import dev.mfazio.pennydrop.types.Player
import dev.mfazio.pennydrop.viewmodels.GameViewModel
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GameViewModelTests {

    /*
        InstantTaskExecutorRule is a JUnit Rule.
        When you use it with the @get:Rule annotation, it causes some code in the InstantTaskExecutorRule class to be run before and after the tests (to see the exact code, you can use the keyboard shortcut Command+B to view the file).

        This rule runs all Architecture Components-related background jobs in the same thread so that the test results happen synchronously, and in a repeatable order.
        When you write tests that include testing LiveData, use this rule!
    */

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var gameViewModel: GameViewModel
    private lateinit var testPlayers: List<Player>

    @Before
    fun initializeViewModel() {
        this.gameViewModel = GameViewModel()

        this.testPlayers = listOf(
            Player("Michael", true),
            Player("Emily", true),
            Player("Hazel", true),
            Player("Riverboat Ron", false, selectedAI = AI.basicAI[5])
        )
    }

    @Test
    fun `Test StartGame on GameViewModel`() {
        this.gameViewModel.slots.value?.let { slots ->
            assertEquals(6, slots.size)
            assertEquals(5, slots.count { it.canBeFilled })
            assertEquals(0, slots.count { it.isFilled })
        } ?: fail("The slots on GameViewModel are null.")

        assertNull(this.gameViewModel.currentPlayer.value)

        assertTrue(this.gameViewModel.canRoll.value == false)
        assertTrue(this.gameViewModel.canPass.value == false)

        assertEquals("", this.gameViewModel.currentTurnText.value)
        assertEquals("", this.gameViewModel.currentStandingsText.value)

        this.gameViewModel.startGame(this.testPlayers)

        this.gameViewModel.slots.value?.let { slots ->
            assertEquals(6, slots.size)
            assertEquals(5, slots.count { it.canBeFilled })
            assertEquals(0, slots.count { it.isFilled })
        } ?: fail("The slots on GameViewModel are null.")

        assertEquals("Michael", this.gameViewModel.currentPlayer.value?.playerName)

        assertTrue(this.gameViewModel.canRoll.value == true)
        assertTrue(this.gameViewModel.canPass.value == false)

        assertEquals("The game has begun!\n", this.gameViewModel.currentTurnText.value)
        assertNotEquals("", this.gameViewModel.currentStandingsText.value)
    }

    @Test
    fun `Test Roll on GameViewModel`() {
        this.gameViewModel.startGame(this.testPlayers)

        this.gameViewModel.roll()

        this.gameViewModel.slots.getOrAwaitValue()?.let { slots ->
            assertNotNull(slots)

            val lastRolledSlot = slots.firstOrNull { it.lastRolled }

            assertNotNull(lastRolledSlot)

            val expectedFilledSlots = if (lastRolledSlot?.number == 6) 0 else 1
            assertEquals(expectedFilledSlots, slots.count { it.isFilled })
            if (expectedFilledSlots > 0) {
                assertEquals(slots.firstOrNull { it.isFilled }, lastRolledSlot)
            }
        } ?: fail("Slots should not be null")

        this.gameViewModel.currentPlayer.getOrAwaitValue()?.let { player ->
            assertEquals("Michael", player.playerName)
            assertEquals(9, player.pennies)
        } ?: fail("No current player was found.")

        this.gameViewModel.canRoll.getOrAwaitValue()?.let { canRoll ->
            assertTrue(canRoll)
        } ?: fail("canRoll should not be null.")
        this.gameViewModel.canPass.getOrAwaitValue()?.let { canPass ->
            assertTrue(canPass)
        } ?: fail("canPass should not be null.")

        this.gameViewModel.currentTurnText.getOrAwaitValue()?.let { turnText ->
            val lastRolledSlot = this.gameViewModel.slots.value?.firstOrNull { it.lastRolled }

            assertTrue(
                turnText.contains("Michael rolled a ${lastRolledSlot?.number}")
            )
        } ?: fail("No current turn text was found.")

        this.gameViewModel.currentStandingsText.getOrAwaitValue()?.let { standingsText ->
            assertTrue(standingsText.contains("Michael - 9 pennies"))
            assertTrue(standingsText.contains("Emily - 10 pennies"))
            assertTrue(standingsText.contains("Hazel - 10 pennies"))
            assertTrue(standingsText.contains("Riverboat Ron - 10 pennies"))
        } ?: fail("No current standings text was found.")
    }
}