package dev.mfazio.abl.api.services

import com.squareup.moshi.Moshi
import dev.mfazio.abl.api.adapters.moshi.MoshiLocalDateAdapter
import dev.mfazio.abl.api.adapters.moshi.MoshiLocalDateTimeAdapter
import dev.mfazio.abl.api.factories.DateTimeQueryConverterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

fun getDefaultABLService(baseUrl: String = "https://abl.mfazio.dev/api/"): AndroidBaseballLeagueService {
    val moshi = Moshi.Builder()
        .add(MoshiLocalDateAdapter())
        .add(MoshiLocalDateTimeAdapter())
        .build()

    val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addConverterFactory(DateTimeQueryConverterFactory())
        .baseUrl(baseUrl)
        .build()

    return retrofit.create(AndroidBaseballLeagueService::class.java)
}