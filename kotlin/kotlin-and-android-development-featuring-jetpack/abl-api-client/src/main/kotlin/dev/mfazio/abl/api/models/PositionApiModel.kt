package dev.mfazio.abl.api.models

enum class PositionApiModel(
    val shortName: String,
    val isOutfield: Boolean = false,
    val isPitcher: Boolean = false,
    val errorChance: Double = 98.6
) {
    StartingPitcher("SP", isPitcher = true, errorChance = 95.2),
    ReliefPitcher("RP", isPitcher = true, errorChance = 95.2),
    Catcher("C", errorChance = 99.4),
    FirstBase("1B", errorChance = 99.4),
    SecondBase("2B", errorChance = 98.2),
    ThirdBase("3B", errorChance = 95.8),
    Shortstop("SS", errorChance = 97.2),
    LeftField("LF", true),
    CenterField("CF", true),
    RightField("RF", true),
    DesignatedHitter("DH"),
    Unknown("N/A");
}