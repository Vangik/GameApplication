package com.example.gameapplication.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

data class ServerResponse(val success: Boolean)

interface WeatherInstance {
    @GET("forecast.json")
    suspend fun getCurrentTemp(
        @Query("key") key: String,
        @Query("q") query: String,
    ): Response<WeatherClass>
}

data class WeatherClass(
    val location: Location? = null
)

data class Location(
    val localtime: String
)

suspend fun makeServerRequest(): Boolean = withContext(Dispatchers.Default) {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.weatherapi.com/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService = retrofit.create(WeatherInstance::class.java)
    val response = apiService.getCurrentTemp("30aee527385d490bb18180841231005", "Lviv")

    return@withContext response.body()?.location?.localtime?.isNotEmpty() == true
}