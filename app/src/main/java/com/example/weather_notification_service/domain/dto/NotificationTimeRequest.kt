package com.example.weather_notification_service.domain.dto

data class NotificationTimeRequest(
    val memberId: String,
    val hours: List<Int>,
)
