package com.example.weather_notification_service.domain.dto

data class TemperatureSettingRequest(
    val memberId: String,
    val minTemp: Float,
    val maxTemp: Float,
)
