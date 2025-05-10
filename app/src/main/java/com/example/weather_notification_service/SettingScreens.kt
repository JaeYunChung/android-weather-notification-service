package com.example.weather_notification_service

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
            Text(text = "${minTemp}°C")
        }
        Text("Alert if above")
        Column {
            Slider(value = maxTemp, onValueChange = { maxTemp = it }, valueRange = 25f..45f)
            Text(text = "${maxTemp}°C")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            coroutineScope.launch {
                try {
                    val request = TemperatureSettingRequest(memberId, minTemp, maxTemp)
                    Log.d("DEBUG", "request: $request")
                    val response = RetrofitClient.apiService.saveSettings(request)
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
    var pm10 by remember { mutableStateOf(80f) }
    var pm25 by remember { mutableStateOf(35f) }
    var message by remember { mutableStateOf(TextFieldValue("Poor air quality. Wear a mask!")) }

    Column(modifier = Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("PM10")
        Column {
            Slider(value = pm10, onValueChange = { pm10 = it }, valueRange = 0f..200f)
            Text(text = "${pm10.toInt()}ppm")
        }
        Text("PM2.5")
        Column {
            Slider(value = pm25, onValueChange = { pm25 = it }, valueRange = 0f..100f)
            Text(text = "${pm25.toInt()}ppm")
        }
        TextField(value = message.text, onValueChange = { message = TextFieldValue(it) })
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {}) { Text("Save") }
    }
}

