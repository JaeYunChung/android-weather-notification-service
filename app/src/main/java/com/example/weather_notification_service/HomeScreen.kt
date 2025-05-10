package com.example.weather_notification_service

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Image(painter = painterResource(android.R.drawable.ic_menu_compass), contentDescription = null, modifier = Modifier.size(80.dp))
        HomeWeatherInfo()
        Image(painter = painterResource(android.R.drawable.ic_menu_gallery), contentDescription = null, modifier = Modifier.size(80.dp))
    }
}

@Composable
fun HomeWeatherInfo() {
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
}
