package dev.mfazio.abl.players

import dev.mfazio.abl.util.convertToInningsPitched
import dev.mfazio.abl.util.round
import androidx.room.Ignore

data class PitcherStats(
    val games: Int = 0,
    val gamesStarted: Int = 0,
    val outs: Int = 0,
    val hits: Int = 0,
    val doubles: Int = 0,
    val triples: Int = 0,
    val homeRuns: Int = 0,
    val runs: Int = 0,
    val earnedRuns: Int = 0,
    val baseOnBalls: Int = 0,
    val hitByPitches: Int = 0,
    val strikeouts: Int = 0,
    val errors: Int = 0,
    val wildPitches: Int = 0,
    val battersFaced: Int = 0,
    val wins: Int = 0,
    val losses: Int = 0,
    val saves: Int = 0,
    val blownSaves: Int = 0,
    val holds: Int = 0
) {
    @Ignore
    val inningsPitched: Double = outs.convertToInningsPitched()

    @Ignore
    val era = (9.0 / inningsPitched * earnedRuns).round(2)

    @Ignore
    val whip = ((hits + baseOnBalls).toDouble() / inningsPitched).round(3)
}