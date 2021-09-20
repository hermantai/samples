package dev.mfazio.abl.api.adapters.moshi

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import dev.mfazio.abl.api.dateTimeHourFormat
import java.time.LocalDateTime

class MoshiLocalDateTimeAdapter {

    @ToJson
    fun toJson(dateTime: LocalDateTime?): String?
        = dateTime?.let { dateTimeHourFormat.format(it) }

    @FromJson
    fun fromJson(dateTimeString: String?): LocalDateTime?
        = dateTimeString?.let { LocalDateTime.parse(it, dateTimeHourFormat) }
}