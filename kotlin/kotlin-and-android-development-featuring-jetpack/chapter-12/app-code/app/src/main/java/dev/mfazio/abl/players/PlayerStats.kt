package dev.mfazio.abl.players

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "stats")
data class PlayerStats(
    val playerId: String,
    @Embedded(prefix = "batter") val batterStats: BatterStats = BatterStats(),
    @Embedded(prefix = "pitcher") val pitcherStats: PitcherStats = PitcherStats()
) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}