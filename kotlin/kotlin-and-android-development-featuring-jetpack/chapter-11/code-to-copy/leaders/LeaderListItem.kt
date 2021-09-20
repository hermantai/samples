package dev.mfazio.abl.leaders

import dev.mfazio.abl.players.Player

data class LeaderListItem(
    val itemId: Long,
    val player: Player,
    val statCategory: String,
    val statValue: String,
    val teamName: String
)