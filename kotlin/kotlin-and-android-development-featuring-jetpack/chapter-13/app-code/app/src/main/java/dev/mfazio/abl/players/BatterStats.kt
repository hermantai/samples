package dev.mfazio.abl.players

import androidx.room.Ignore
import dev.mfazio.abl.util.round

data class BatterStats(
    val games: Int = 0,
    val plateAppearances: Int = 0,
    val atBats: Int = 0,
    val runs: Int = 0,
    val hits: Int = 0,
    val doubles: Int = 0,
    val triples: Int = 0,
    val homeRuns: Int = 0,
    val rbi: Int = 0,
    val strikeouts: Int = 0,
    val baseOnBalls: Int = 0,
    val hitByPitch: Int = 0,
    val stolenBases: Int = 0,
    val caughtStealing: Int = 0,
    val gidp: Int = 0,
    val sacrificeHits: Int = 0,
    val sacrificeFlies: Int = 0,
    val errors: Int = 0,
    val passedBalls: Int = 0
) {
    @Ignore
    val totalBases = (hits + doubles + triples * 2 + homeRuns * 3)

    @Ignore
    val battingAverage = if(hits <= 0 || atBats <= 0) 0.0 else (hits.toDouble() / atBats.toDouble()).round(3)

    @Ignore
    val onBasePercentage = if(hits <= 0 || atBats <= 0) 0.0 else
        ((hits.toDouble() + baseOnBalls.toDouble() + hitByPitch.toDouble()) / plateAppearances.toDouble()).round(3)

    @Ignore
    val sluggingPercentage = if(hits <= 0 || atBats <= 0) 0.0 else (totalBases.toDouble() / atBats.toDouble()).round(3)

    @Ignore
    val ops = onBasePercentage + sluggingPercentage
}