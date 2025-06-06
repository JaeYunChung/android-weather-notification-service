package com.example.weather_notification_service.domain.dto

data class NotificationTimeRequest(
    val memberId: String,
    val startHour: Int,
    val endHour: Int,
)
