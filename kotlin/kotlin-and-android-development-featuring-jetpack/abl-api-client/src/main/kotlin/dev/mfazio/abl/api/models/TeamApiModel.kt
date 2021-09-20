package dev.mfazio.abl.api.models

data class TeamApiModel(
    val id: String,
    val location: String,
    val nickname: String,
    val division: DivisionApiModel,
    val logo: String,
    val scheduleId: String = ""
)