package com.example.weather_notification_service

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.weather_notification_service.setting_screen.AirQualitySettingsScreen
import com.example.weather_notification_service.setting_screen.NotificationSettingsScreen
import com.example.weather_notification_service.setting_screen.TemperatureSettingsScreen

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeatherAlarmApp(activity: MainActivity) {
    var screen by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Weather Alarm") }
            )
        },
        bottomBar = {
            BottomNavigationBar(currentScreen = screen) { screen = it }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (screen) {
                0 -> HomeScreen(activity)
                1 -> NotificationSettingsScreen()
                2 -> TemperatureSettingsScreen()
                3 -> AirQualitySettingsScreen()
            }
        }
    }
}



@Composable
fun BottomNavigationBar(currentScreen: Int, onTabSelected: (Int) -> Unit) {
    val tabs = listOf(
        Icons.Default.Home to "Home",
        Icons.Default.Notifications to "Notify",
        Icons.Default.Thermostat to "Temp",
        Icons.Default.Air to "Air"
    )
    NavigationBar {
        tabs.forEachIndexed { index, pair ->
            NavigationBarItem(
                selected = currentScreen == index,
                onClick = { onTabSelected(index) },
                icon = { Icon(pair.first, contentDescription = pair.second) },
                label = { Text(pair.second) }
            )
        }
    }
}