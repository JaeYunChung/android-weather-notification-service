package com.example.weather_notification_service.setting_screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.weather_notification_service.connection.RetrofitClient
import com.example.weather_notification_service.domain.DustSettingEntity
import kotlinx.coroutines.launch

@Composable
fun AirQualitySettingsScreen() {
    Column(modifier = Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        val coroutineScope = rememberCoroutineScope()
        val context = LocalContext.current
        Text("미세먼지 등급 선택")
        val options = listOf("좋음", "보통", "나쁨")
        var selectedQualityPM10 by remember { mutableStateOf("보통") }
        Text("pm10 ")
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            options.forEach { level ->
                Button(
                    onClick = { selectedQualityPM10 = level },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedQualityPM10 == level) Color(0xFF6A1B9A) else Color.LightGray
                    )
                ) {
                    Text(level, color = Color.White)
                }
            }
        }
        Text("선택된 등급: ${selectedQualityPM10}")
        var selectedQualityPM25 by remember { mutableStateOf("보통") }
        Text("pm25 ")
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            options.forEach { level ->
                Button(
                    onClick = { selectedQualityPM25 = level },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedQualityPM25 == level) Color(0xFF6A1B9A) else Color.LightGray
                    )
                ) {
                    Text(level, color = Color.White)
                }
            }
        }
        Text("선택된 등급: ${selectedQualityPM25}")
        Text("선택한 등급 이상일 때 알림이 발송됩니다!")
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            coroutineScope.launch {
                try {
                    val request = DustSettingEntity(memberId, selectedQualityPM10, selectedQualityPM25)
                    Log.d("DEBUG", "request: $request")
                    val response = RetrofitClient.apiService.saveDustSettings(request)
                    Log.d("DEBUG", "Response: $response")
                    if (response.isSuccessful) {
                        Toast.makeText(context, "Settings saved", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Failed to save settings", Toast.LENGTH_SHORT).show()
                    }
                }catch (e: Exception) {
                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }}
        }) { Text("Save") }
    }
}
