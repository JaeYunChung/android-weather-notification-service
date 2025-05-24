package com.example.weather_notification_service.setting_screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
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
import com.example.weather_notification_service.domain.dto.TemperatureSettingRequest
import kotlinx.coroutines.launch

@Composable
fun TemperatureSettingsScreen() {
    var minTemp by remember { mutableStateOf(10f) }
    var maxTemp by remember { mutableStateOf(30f) }

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