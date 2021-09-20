package dev.mfazio.pennydrop.data

import androidx.lifecycle.LiveData
import androidx.room.*
import dev.mfazio.pennydrop.types.Player
import java.time.OffsetDateTime

@Dao
abstract class PennyDropDao {

    @Query("SELECT * FROM players WHERE playerName = :playerName")
    abstract fun getPlayer(playerName: String): Player?

    /* This is a transaction since GameWithPlayers pulls from multiple tables. */
    @Transaction
    @Query("SELECT *  FROM games ORDER BY startTime DESC LIMIT 1")
    abstract fun getCurrentGameWithPlayers(): LiveData<GameWithPlayers>

    @Transaction
    @Query(
        """
        SELECT * FROM game_statuses
        WHERE gameId = (
            SELECT gameId FROM games
            ORDER BY startTime DESC
            LIMIT 1)
        ORDER BY gamePlayerNumber
        """
    )
    abstract fun getCurrentGameStatuses(): LiveData<List<GameStatus>>

    @Insert
    abstract suspend fun insertGame(game: Game): Long

    @Insert
    abstract suspend fun insertPlayer(player: Player): Long

    @Insert
    abstract suspend fun insertPlayers(players: List<Player>): List<Long>

    @Insert
    abstract suspend fun insertGameStatuses(gameStatuses: List<GameStatus>)

    @Update
    abstract suspend fun updateGame(game: Game)

    @Update
    abstract suspend fun updateGameStatuses(gameStatuses: List<GameStatus>)

    @Query(
        """
        UPDATE games
        SET endTime = :endDate, gameState = :gameState
        WHERE endTime IS NULL
        """
    )
    abstract suspend fun closeOpenGames(
        endDate: OffsetDateTime = OffsetDateTime.now(),
        gameState: GameState = GameState.Cancelled
    )

    @Transaction
    open suspend fun startGame(players: List<Player>): Long {
        this.closeOpenGames()

        val gameId = this.insertGame(
            Game(
                gameState = GameState.Started,
                currentTurnText = "The game has begun!\n",
                canRoll = true
            )
        )

        val playerIds = players.map { player ->
            getPlayer(player.playerName)?.playerId ?: insertPlayer(player)
        }

        this.insertGameStatuses(
            playerIds.mapIndexed { index, playerId ->
                GameStatus(
                    gameId,
                    playerId,
                    index,
                    index == 0
                )
            }
        )

        return gameId
    }

    @Transaction
    open suspend fun updateGameAndStatuses(game: Game, statuses: List<GameStatus>) {
        this.updateGame(game)
        this.updateGameStatuses(statuses)
    }
}