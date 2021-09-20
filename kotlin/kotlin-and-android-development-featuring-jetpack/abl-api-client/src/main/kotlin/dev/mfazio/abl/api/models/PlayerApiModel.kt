package dev.mfazio.abl.api.models

data class PlayerApiModel(
    val playerId: String,
    val teamId: String,
    val firstName: String,
    val lastName: String,
    val number: Int = 0,
    val bats: HandApiModel = HandApiModel.Right,
    val throws: HandApiModel = HandApiModel.Right,
    val position: PositionApiModel = PositionApiModel.Unknown,
    val boxScoreLastName: String?
)