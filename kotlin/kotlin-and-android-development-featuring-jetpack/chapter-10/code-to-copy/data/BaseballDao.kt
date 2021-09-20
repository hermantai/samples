package dev.mfazio.abl.data

import androidx.lifecycle.LiveData
import androidx.room.*
import dev.mfazio.abl.scoreboard.ScheduledGame
import dev.mfazio.abl.standings.TeamStanding

@Dao
abstract class BaseballDao {
    @Query("SELECT * FROM games WHERE gameId LIKE :dateString")
    abstract fun getGamesForDate(dateString: String): LiveData<List<ScheduledGame>>

    @Query("SELECT * FROM games WHERE gameId = :gameId")
    abstract fun getGameByGameId(gameId: String): ScheduledGame?

    @Insert
    abstract suspend fun insertGame(game: ScheduledGame)

    @Update
    abstract suspend fun updateGame(game: ScheduledGame)

    @Transaction
    open suspend fun insertOrUpdateGames(games: List<ScheduledGame>) {
        games.forEach { game ->
            getGameByGameId(game.gameId)?.let { dbGame ->
                updateGame(game.apply { id = dbGame.id })
            } ?: insertGame(game)
        }
    }

    @Insert
    abstract suspend fun insertStandings(standings: List<TeamStanding>)

    @Update
    abstract suspend fun updateStandings(standings: List<TeamStanding>)

    @Query("SELECT * FROM standings")
    abstract fun getStandings(): LiveData<List<TeamStanding>>

    //NOTE: This isn't ideal, being a copy of the above function
    //However, it's required since LiveData<*> requires an observer
    //By adding this version (note the `suspend` modifier), we can just get the values from the DB.
    @Query("SELECT * FROM standings")
    abstract suspend fun getCurrentStandings(): List<TeamStanding>
}