package dev.mfazio.abl.api

import java.time.format.DateTimeFormatter

val dateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
val dateTimeHourFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHH")
val dateTimeFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")