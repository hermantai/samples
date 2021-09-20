package dev.mfazio.abl.api.adapters.moshi

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import dev.mfazio.abl.api.dateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MoshiLocalDateAdapter {

    @ToJson
    fun toJson(date: LocalDate?): String?
        = date?.let { dateFormat.format(it) }

    @FromJson
    fun fromJson(dateString: String?): LocalDate?
        = dateString?.let { LocalDate.parse(it, dateFormat) }
}