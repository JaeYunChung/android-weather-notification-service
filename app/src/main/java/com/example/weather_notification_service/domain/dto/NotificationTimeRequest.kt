package com.example.weather_notification_service.domain.dto

data class NotificationTimeRequest(
    val memberId: String,
    /**
     * List of times in HH:mm format
     */
    val times: List<String>,
)
