package com.example.weather_notification_service

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity

import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.weather_notification_service.connection.RetrofitClient
import com.example.weather_notification_service.domain.dto.NotificationTokenDto
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                Log.d("FCM", "Device Token: $token")
                val dto = NotificationTokenDto(token)
                lifecycleScope.launch {
                    try {
                        val response = RetrofitClient.apiService.sendNotificationToken("hello", dto)
                        if (response.isSuccessful) {
                            Log.d("FCM", "Token sent")
                        } else {
                            Log.e("FCM", "Fail code: ${response.code()}")
                        }
                    } catch (e: Exception) {
                        Log.e("FCM", "Retrofit 예외", e)
                    }
                }
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
            }
        }
        setContent {
            MaterialTheme(colorScheme = lightColorScheme()) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WeatherAlarmApp(this)
                }
            }
        }


    }
}