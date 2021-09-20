package dev.mfazio.abl.players

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "players")
data class Player(
    val playerId: String,
    val teamId: String,
    val firstName: String,
    val lastName: String,
    val number: Int = 0,
    val batsWith: Hand = Hand.Right,
    val throwsWith: Hand = Hand.Right,
    val position: Position = Position.Unknown,
    val boxScoreLastName: String?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    @Ignore
    val fullName = "$firstName $lastName"
}

enum class Hand { Left, Right, Unknown }

enum class Position(val shortName: String) {
    StartingPitcher("SP"),
    ReliefPitcher("RP"),
    Catcher("C"),
    FirstBase("1B"),
    SecondBase("2B"),
    ThirdBase("3B"),
    Shortstop("SS"),
    LeftField("LF"),
    CenterField("CF"),
    RightField("RF"),
    DesignatedHitter("DH"),
    Unknown("N/A");

    fun isPitcher() = this == StartingPitcher || this == ReliefPitcher
}