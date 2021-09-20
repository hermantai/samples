package dev.mfazio.abl.util

import android.app.Application
import dev.mfazio.abl.R
import dev.mfazio.abl.data.BaseballRepository.*
import dev.mfazio.abl.scoreboard.ScheduledGame
import dev.mfazio.abl.scoreboard.ScheduledGameStatus
import java.text.DecimalFormat

fun Int.withOrdinal() =
    this.toString() + if (this % 100 in 11..13) "th" else when (this % 10) {
        1 -> "st"
        2 -> "nd"
        3 -> "rd"
        else -> "th"
    }

fun Double.toERAString() = DecimalFormat("0.00").format(this)

fun Double.toBattingPercentageString() = DecimalFormat("#.000").format(this)

val ScheduledGame?.inningText: String
    get() = if (this != null) "${if (this.isTopOfInning == true) "Top" else "Bot"} ${this.inning?.withOrdinal()}"
    else "N/A"

val ScheduledGame?.gameStartTimeText: String
    get() = if (this != null) ScheduledGame.startTimeFormat.format(this.gameStartTime) else "N/A"

fun ScheduledGame?.getPlayerLabel(playerNum: Int) =
    when (this?.gameStatus) {
        ScheduledGameStatus.Upcoming -> listOf(this.awayTeamId, this.homeTeamId, null)[playerNum]
        ScheduledGameStatus.InProgress -> listOf("P", "AB", null)[playerNum]
        ScheduledGameStatus.Completed -> listOf("W", "L", "S")[playerNum]
        else -> null
    }

fun ResultStatus.getErrorMessage(application: Application) = when (this) {
    ResultStatus.NetworkException -> application.resources.getString(R.string.network_exception_message)
    ResultStatus.RequestException -> application.resources.getString(R.string.request_exception_message)
    ResultStatus.GeneralException -> application.resources.getString(R.string.general_exception_message)
    else -> null
}