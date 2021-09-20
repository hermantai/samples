package dev.mfazio.pennydrop

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.mfazio.pennydrop.data.PennyDropDao
import dev.mfazio.pennydrop.data.PennyDropDatabase
import dev.mfazio.pennydrop.game.AI
import dev.mfazio.pennydrop.types.Player
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.Executors

@RunWith(AndroidJUnit4::class)
class PennyDropDaoTests {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: PennyDropDatabase
    private lateinit var dao: PennyDropDao

    @Before
    fun initializeDatabaseAndDao() {
        this.database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            PennyDropDatabase::class.java
        )
            .allowMainThreadQueries()
            .setTransactionExecutor(Executors.newSingleThreadExecutor())
            .build()

        this.dao = this.database.pennyDropDao()
    }

    @After
    fun closeDatabase() = database.close()

    @Test
    fun testInsertingNewPlayer() = runBlocking {
        val player = Player(5, "Hazel")

        assertNull(dao.getPlayer(player.playerName))

        val insertedPlayerId = dao.insertPlayer(player)

        assertEquals(player.playerId, insertedPlayerId)

        dao.getPlayer(player.playerName)?.let { newPlayer ->
            assertEquals(player.playerId, newPlayer.playerId)
            assertEquals(player.playerName, newPlayer.playerName)
            assertTrue(player.isHuman)
        } ?: fail("New player not found.")
    }

    @Test
    fun testStartGame() = runBlocking {
        val players = listOf(
            Player(23, "Michael"),
            Player(12, "Emily"),
            Player(5, "Hazel"),
            Player(100, "Even Steven", false, AI.basicAI[4])
        )
        val pennyCount = 15

        val gameId = dao.startGame(players, pennyCount)

        dao.getCurrentGameWithPlayers().getOrAwaitValue()?.let { gameWithPlayers ->
            with(gameWithPlayers.game) {
                assertEquals(gameId, this.gameId)
                assertNotNull(startTime)
                assertNull(endTime)
                assertNull(lastRoll)
                assertTrue(canRoll)
                assertFalse(canPass)
            }

            val gamePlayers = gameWithPlayers.players

            players.forEach { player ->
                assertTrue(gamePlayers.contains(player))
            }

        } ?: fail("No current game with players found.")

        players.map { it.playerName }.forEach { playerName ->
            assertNotNull(dao.getPlayer(playerName))
        }

        val playerIds = players.map { it.playerId }
        dao.getCurrentGameStatuses().getOrAwaitValue()?.let { gameStatuses ->
            assertTrue(gameStatuses.all { it.gameId == gameId })
            assertTrue(gameStatuses.all { playerIds.contains(it.playerId) })
            assertTrue(gameStatuses.all { it.pennies == pennyCount })
            assertEquals(1, gameStatuses.count { it.isRolling })
            assertEquals(players.first().playerId, gameStatuses.first { it.isRolling }.playerId)
        } ?: fail("No current game with players found.")
    }
}