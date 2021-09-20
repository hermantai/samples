package dev.mfazio.abl.players

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "player_list_items")
data class PlayerListItem(
    @PrimaryKey val playerId: String,
    val playerName: String,
    val teamId: String,
    val position: Position
)