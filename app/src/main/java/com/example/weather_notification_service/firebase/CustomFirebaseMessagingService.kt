package com.example.weather_notification_service.firebase

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.icu.util.ULocale.Category
import android.os.Build
import android.util.Log
import androidx.compose.runtime.rememberCoroutineScope
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import com.example.weather_notification_service.R
import com.example.weather_notification_service.connection.RetrofitClient
import com.example.weather_notification_service.domain.dto.NotificationTokenDto
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


class CustomFirebaseMessagingService : FirebaseMessagingService() {
    private val serviceJob = SupervisorJob()
    private val serviceScope = CoroutineScope(Dispatchers.IO + serviceJob)
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "Refreshed token: $token")
        val dto = NotificationTokenDto(token);

        serviceScope.launch {
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
        // 이 토큰을 앱 서버로 전송하여 저장해야 합니다.
        // 예: sendRegistrationToServer(token)
    }
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        remoteMessage.notification?.let {
            showNotification(it.title ?: "알림", it.body ?: "")
        }

        Log.d("FCM", "From: ${remoteMessage.from}")

        // 알림 메시지가 포함된 경우
        remoteMessage.notification?.let {
            Log.d("FCM", "Notification Message Body: ${it.body}")
        }

        // 데이터 메시지가 포함된 경우
        remoteMessage.data.isNotEmpty().let {
            Log.d("FCM", "Data Message: ${remoteMessage.data}")
        }

    }
    private fun showNotification(title: String, message: String) {
        val channelId = "fcm_default_channel"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "FCM Channel",
                NotificationManager.IMPORTANCE_HIGH,
            ).apply {
                enableVibration(true)
                enableLights(true)
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                setShowBadge(true)
            }
            notificationManager.createNotificationChannel(channel)

        }


        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.notification_inc) // 반드시 존재해야 함
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setAutoCancel(true)
            .build()

        try {
            notificationManager.notify(0, notification)
            Log.d("FCM", "알림 생성 성공")
        } catch (e: Exception) {
            Log.e("FCM", "알림 생성 실패: ${e.message}")
        }

    }
}