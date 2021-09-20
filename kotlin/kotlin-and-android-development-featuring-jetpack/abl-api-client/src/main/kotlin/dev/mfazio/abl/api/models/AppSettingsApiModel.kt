package dev.mfazio.abl.api.models

data class AppSettingsApiModel(
    val userId: String,
    val favoriteTeamId: String,
    val useTeamColorNavBar: Boolean,
    val startingLocation: String
)
