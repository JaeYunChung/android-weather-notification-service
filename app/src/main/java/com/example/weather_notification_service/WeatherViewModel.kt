package com.example.weather_notification_service

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.json.JSONObject

class WeatherViewModel:ViewModel() {
    val messageLive = MutableLiveData<JSONObject>()
}