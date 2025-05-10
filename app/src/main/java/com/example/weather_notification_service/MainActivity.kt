package com.example.weather_notification_service

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(colorScheme = lightColorScheme()) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WeatherAlarmApp()
                }
            }
        }
    }
}

@Composable
fun WeatherAlarmApp() {
    var screen by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val tabs = listOf("Home", "Notify", "Temp", "Air")
            tabs.forEachIndexed { index, label ->
                Button(
                    onClick = { screen = index },
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A1B9A))
                ) {
                    Text(label, color = Color.White, fontSize = 14.sp)
                }
            }
        }
        when (screen) {
            0 -> HomeScreen()
            1 -> NotificationSettingsScreen()
            2 -> TemperatureSettingsScreen()
            3 -> AirQualitySettingsScreen()
        }
    }
}

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Image(
            painter = painterResource(android.R.drawable.ic_menu_compass), // placeholder for sun icon
            contentDescription = null,
            modifier = Modifier.size(80.dp)
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("May 24, 2024", fontSize = 20.sp)
            Text("7:30 AM", fontSize = 18.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text("🌧", fontSize = 42.sp)
            Text("Rain", fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Rain expected today, take an umbrella!", fontSize = 16.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Alert ON", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            Text("Air Quality Alert: ON", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
        }
        Image(
            painter = painterResource(android.R.drawable.ic_menu_gallery), // placeholder for cloud/mask icon
            contentDescription = null,
            modifier = Modifier.size(80.dp)
        )
    }
}

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
fun TemperatureSettingsScreen() {
    var minTemp by remember { mutableStateOf(10f) }
    var maxTemp by remember { mutableStateOf(30f) }
    var message by remember { mutableStateOf(TextFieldValue("Take a jacket if below 10°")) }

    Column(modifier = Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Alert if below")
        Slider(value = minTemp, onValueChange = { minTemp = it }, valueRange = -10f..20f)
        Text("Alert if above")
        Slider(value = maxTemp, onValueChange = { maxTemp = it }, valueRange = 25f..45f)
        TextField(value = message.text, onValueChange = { message = TextFieldValue(it) })
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {}) { Text("Save") }
    }
}

@Composable
fun AirQualitySettingsScreen() {
    var pm10 by remember { mutableStateOf(80f) }
    var pm25 by remember { mutableStateOf(35f) }
    var message by remember { mutableStateOf(TextFieldValue("Poor air quality. Wear a mask!")) }

    Column(modifier = Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("PM10")
        Slider(value = pm10, onValueChange = { pm10 = it }, valueRange = 0f..200f)
        Text("PM2.5")
        Slider(value = pm25, onValueChange = { pm25 = it }, valueRange = 0f..100f)
        TextField(value = message.text, onValueChange = { message = TextFieldValue(it) })
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {}) { Text("Save") }
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