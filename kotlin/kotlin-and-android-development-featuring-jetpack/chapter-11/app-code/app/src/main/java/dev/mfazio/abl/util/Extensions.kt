package dev.mfazio.abl.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun Double.round(places: Int): Double {
    var multiplier = 1.0
    repeat(places) { multiplier *= 10 }
    return kotlin.math.round(this * multiplier) / multiplier
}

fun Int.convertToInningsPitched() = (this / 3) + (this % 3 / 10.0)

fun LocalDate.toGameDateString() = this.format(gameDateFormat)

private val gameDateFormat = DateTimeFormatter.ofPattern("yyyyMMdd")