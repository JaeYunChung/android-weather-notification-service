package com.example.weather_notification_service.connection


import com.example.weather_notification_service.domain.CustomSettingEntity
import com.example.weather_notification_service.domain.CustomSettingResponse
import com.example.weather_notification_service.domain.DustSettingEntity
import com.example.weather_notification_service.domain.TemperatureSettingRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("/temp/setting/")
    suspend fun saveTempSettings(@Body request: TemperatureSettingRequest): Response<Unit>

    @POST("/dust/setting/")
    suspend fun saveDustSettings(@Body request: DustSettingEntity):Response<Unit>

    @POST("/custom/setting/")
    suspend fun saveCustomSettings(@Body request: CustomSettingEntity):Response<Unit>

    @GET("/custom/setting/")
    suspend fun getCustomSettings(@Query("memberId") memberId:String):Response<CustomSettingResponse>
}