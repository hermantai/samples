package dev.mfazio.abl.data

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.*
import dev.mfazio.abl.players.*
import dev.mfazio.abl.scoreboard.ScheduledGame
import dev.mfazio.abl.standings.TeamStanding

@Dao
abstract class BaseballDao {
    @Insert
    abstract suspend fun insertStandings(standings: List<TeamStanding>)

    @Update
    abstract suspend fun updateStandings(standings: List<TeamStanding>)

    @Query("SELECT * FROM standings")
    abstract fun getStandings(): LiveData<List<TeamStanding>>

    @Query("SELECT * FROM standings")
    abstract suspend fun getCurrentStandings(): List<TeamStanding>

    @Query("SELECT * FROM games WHERE gameId LIKE :dateString")
    abstract fun getGamesForDate(
        dateString: String
    ): LiveData<List<ScheduledGame>>

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

    @Query("SELECT * FROM players WHERE playerId = :playerId")
    abstract fun getPlayerById(playerId: String): Player?

    @Insert
    abstract suspend fun insertPlayer(player: Player)

    @Update
    abstract suspend fun updatePlayer(player: Player)

    @Transaction
    open suspend fun insertOrUpdatePlayers(players: List<Player>) {
        players.forEach { player -> insertOrUpdatePlayer(player) }
    }

    @Transaction
    open suspend fun insertOrUpdatePlayer(player: Player) {
        getPlayerById(player.playerId)?.let { dbPlayer ->
            updatePlayer(player.apply { id = dbPlayer.id })
        } ?: insertPlayerAndStats(player)
    }

    @Transaction
    open suspend fun insertPlayerAndStats(player: Player) {
        insertPlayer(player)
        if (getPlayerStatsById(player.playerId) == null) {
            insertPlayerStats(PlayerStats(player.playerId))
        }
    }

    @Query("DELETE FROM players")
    abstract suspend fun deleteAllPlayers()

    @Query("SELECT * FROM stats WHERE playerId = :playerId")
    abstract suspend fun getPlayerStatsById(playerId: String): PlayerStats?

    @Insert
    abstract suspend fun insertPlayerStats(stats: PlayerStats)

    @Update
    abstract suspend fun updatePlayerStats(stats: PlayerStats)

    @Transaction
    @Query("SELECT * FROM players WHERE playerId = :playerId")
    abstract fun getPlayerWithStats(playerId: String): LiveData<PlayerWithStats?>

    @Transaction
    @Query("SELECT * FROM players WHERE position NOT IN (:pitcherPositions)")
    abstract fun getBattersWithStats(
        pitcherPositions: List<Position> = listOf(
            Position.StartingPitcher,
            Position.ReliefPitcher
        )
    ): LiveData<List<PlayerWithStats>?>

    @Transaction
    @Query("SELECT * FROM players WHERE position IN (:pitcherPositions)")
    abstract fun getPitchersWithStats(
        pitcherPositions: List<Position> = listOf(
            Position.StartingPitcher,
            Position.ReliefPitcher
        )
    ): LiveData<List<PlayerWithStats>?>

    @Transaction
    open suspend fun insertOrUpdateStats(playerStats: List<PlayerStats>) {
        playerStats.forEach { stats ->
            insertOrUpdatePlayerStats(stats)
        }
    }

    @Transaction
    open suspend fun insertOrUpdatePlayerStats(playerStats: PlayerStats) {
        getPlayerStatsById(playerStats.playerId)?.let { dbPlayerStats ->
            updatePlayerStats(playerStats.apply { id = dbPlayerStats.id })
        } ?: insertPlayerStats(playerStats)
    }

    @Query(
        """
            SELECT * FROM player_list_items
            WHERE (:teamId IS NULL OR teamId = :teamId)
            AND (:nameQuery IS NULL OR playerName LIKE :nameQuery)
            ORDER BY playerId
        """
    )

    abstract fun getPlayerListItems(
        teamId: String? = null,
        nameQuery: String? = null
    ): PagingSource<Int, PlayerListItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertPlayerListItems(
        playerListItems: List<PlayerListItem>
    )

    @Query("DELETE FROM player_list_items")
    abstract suspend fun deleteAllPlayerListItems()
}