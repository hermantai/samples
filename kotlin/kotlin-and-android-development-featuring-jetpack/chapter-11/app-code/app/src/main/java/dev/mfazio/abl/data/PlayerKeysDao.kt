package dev.mfazio.abl.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.mfazio.abl.players.PlayerKeys

@Dao
interface PlayerKeysDao {

    @Query("SELECT * FROM player_keys WHERE playerId = :playerId")
    suspend fun getPlayerKeysByPlayerId(playerId: String): PlayerKeys?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKeys(keys: List<PlayerKeys>)

    @Query("DELETE FROM player_keys")
    suspend fun deleteAllPlayerKeys()

}