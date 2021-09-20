package dev.mfazio.abl.api.factories

import dev.mfazio.abl.api.dateFormat
import dev.mfazio.abl.api.dateTimeFormat
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.LocalDateTime

class DateTimeQueryConverterFactory : Converter.Factory() {
    override fun stringConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<*, String>? = when (type) {
        LocalDate::class.java -> Converter<LocalDate, String> { date -> dateFormat.format(date) }
        LocalDateTime::class.java -> Converter<LocalDateTime, String> { dateTime -> dateTimeFormat.format(dateTime) }
        else -> super.stringConverter(type, annotations, retrofit)
    }
}