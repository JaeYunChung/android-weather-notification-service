package com.example.weather_notification_service.setting_screen

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
        val sharedPref = context.getSharedPreferences("dust_settings", Context.MODE_PRIVATE)
        Text("미세먼지 등급 선택")
        val options = listOf("좋음", "보통", "나쁨")
        var selectedQualityPM10 by remember { mutableStateOf(sharedPref.getString("pm10_level", "보통")?:"보통") }
        Text("pm10 ")
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            options.forEach { level ->
                FilterChip(
                    selected = selectedQualityPM10 == level,
                    onClick = { selectedQualityPM10 = level },
                    label = { Text(level) },
                    leadingIcon = if (selectedQualityPM10 == level) {
                        { Icon(Icons.Default.Done, contentDescription = null) }
                    } else null,
                    colors = FilterChipDefaults.filterChipColors()
                )
            }
        }
        Text("선택된 등급: ${selectedQualityPM10}")
        var selectedQualityPM25 by remember { mutableStateOf(sharedPref.getString("pm25_level", "보통")?:"보통") }
        Text("pm25 ")
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            options.forEach { level ->
                FilterChip(
                    selected = selectedQualityPM25 == level,
                    onClick = { selectedQualityPM25 = level },
                    label = { Text(level) },
                    leadingIcon = if (selectedQualityPM25 == level) {
                        { Icon(Icons.Default.Done, contentDescription = null) }
                    } else null,
                    colors = FilterChipDefaults.filterChipColors(),
                )
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

                    with(sharedPref.edit()) {
                        putString("pm10_level", selectedQualityPM10)
                        putString("pm25_level", selectedQualityPM25)
                        apply()
                    }

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
