package dev.mfazio.abl.data

import androidx.room.TypeConverter
import dev.mfazio.abl.scoreboard.OccupiedBases
import dev.mfazio.abl.scoreboard.ScheduledGameStatus
import dev.mfazio.abl.standings.WinLoss
import dev.mfazio.abl.teams.Division
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class BaseballConverters {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    @TypeConverter
    fun fromDivision(division: Division?) =
        division?.ordinal ?: Division.Unknown.ordinal

    @TypeConverter
    fun toDivision(divisionOrdinal: Int?) =
        if (divisionOrdinal != null) {
            Division.values()[divisionOrdinal]
        } else {
            Division.Unknown
        }

    @TypeConverter
    fun fromWinLoss(winLoss: WinLoss?) =
        winLoss?.ordinal ?: WinLoss.Unknown.ordinal

    @TypeConverter
    fun toWinLoss(winLossOrdinal: Int?) =
        if (winLossOrdinal != null) {
            WinLoss.values()[winLossOrdinal]
        } else {
            WinLoss.Unknown
        }

    @TypeConverter
    fun toLocalDateTime(value: String?) = value?.let {
        formatter.parse(it, LocalDateTime::from)
    }

    @TypeConverter
    fun fromLocalDateTime(date: LocalDateTime?) = date?.format(formatter)

    @TypeConverter
    fun fromScheduledGameStatus(status: ScheduledGameStatus) = status.ordinal

    @TypeConverter
    fun toScheduledGameStatus(statusOrdinal: Int) = ScheduledGameStatus.values()[statusOrdinal]

    @TypeConverter
    fun fromOccupiedBases(bases: OccupiedBases?) = bases?.toStringList()

    @TypeConverter
    fun toOccupiedBases(basesStringList: String?) = OccupiedBases.fromStringList(basesStringList)
}