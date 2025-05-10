package com.example.weather_notification_service


import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface TemperatureApiService {
    @POST("/temp/setting/")
    suspend fun saveSettings(@Body request: TemperatureSettingRequest): Response<Unit>

}