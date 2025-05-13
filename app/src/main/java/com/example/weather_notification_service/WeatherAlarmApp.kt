package com.example.weather_notification_service

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeatherAlarmApp(activity: MainActivity) {
    var screen by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TabRow(screen) { screen = it }
        when (screen) {
            0 -> HomeScreen(activity)
            1 -> NotificationSettingsScreen()
            2 -> TemperatureSettingsScreen()
            3 -> AirQualitySettingsScreen()
        }
    }
}



@Composable
fun TabRow(currentScreen: Int, onTabSelected: (Int) -> Unit) {
    val tabs = listOf("Home", "Notify", "Temp", "Air")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        tabs.forEachIndexed { index, label ->
            Button(
                onClick = { onTabSelected(index) },
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A1B9A))
            ) {
                Text(label, color = Color.White, fontSize = 14.sp)
            }
        }
    }
}