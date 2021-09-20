package dev.mfazio.abl.api.models

data class BoxScoreItemsApiModel(
    val batting: BatterBoxScoreItemApiModel? = null,
    val pitching: PitcherBoxScoreItemApiModel? = null
)