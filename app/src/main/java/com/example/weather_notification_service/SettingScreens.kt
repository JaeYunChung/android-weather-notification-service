package com.example.weather_notification_service

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.TextFieldValue
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun NotificationSettingsScreen() {
    var rainAlert by remember { mutableStateOf(true) }
    var airAlert by remember { mutableStateOf(true) }
    var tempAlert by remember { mutableStateOf(true) }
    var weekendOff by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        SettingRow("Get alerts for rain", rainAlert) { rainAlert = it }
        SettingRow("Get alerts for poor air quality", airAlert) { airAlert = it }
        SettingRow("Get clothing alerts for low temp", tempAlert) { tempAlert = it }
        SettingRow("Turn off alerts on weekends", weekendOff) { weekendOff = it }
    }
}

@Composable
fun SettingRow(label: String, checked: Boolean, onToggle: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label)
        Spacer(Modifier.weight(1f))
        Switch(checked = checked, onCheckedChange = onToggle)
    }
}
@Composable
fun TemperatureSettingsScreen() {
    var memberId by remember { mutableStateOf("hello") }
    var minTemp by remember { mutableStateOf(10f) }
    var maxTemp by remember { mutableStateOf(30f) }
    var message by remember { mutableStateOf(TextFieldValue("Take a jacket if below 10°")) }

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Alert if below")
        Column {
            Slider(value = minTemp, onValueChange = { minTemp = it }, valueRange = -10f..20f)
            Text(text = "${minTemp.toInt()}°C")
        }
        Text("Alert if above")
        Column {
            Slider(value = maxTemp, onValueChange = { maxTemp = it }, valueRange = 25f..45f)
            Text(text = "${maxTemp.toInt()}°C")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            coroutineScope.launch {
                try {
                    val request = TemperatureSettingRequest(memberId, minTemp, maxTemp)
                    Log.d("DEBUG", "request: $request")
                    val response = RetrofitClient.apiService.saveTempSettings(request)
                    Log.d("DEBUG", "Response: $response")
                    if (response.isSuccessful) {
                        Toast.makeText(context, "Settings saved", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Failed to save settings", Toast.LENGTH_SHORT).show()
                    }
            }catch (e: Exception) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }}
        })
        { Text("Save") }
    }
}

@Composable
fun AirQualitySettingsScreen() {
    Column(modifier = Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
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
        Button(onClick = {}) { Text("Save") }
    }
}

