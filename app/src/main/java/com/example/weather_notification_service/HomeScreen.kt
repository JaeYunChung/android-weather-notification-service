package com.example.weather_notification_service

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weather_notification_service.connection.WebSocketClient

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(activity: MainActivity) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Image(
            painter = painterResource(android.R.drawable.ic_menu_compass),
            contentDescription = null,
            modifier = Modifier.size(80.dp)
        )
        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            HomeWeatherInfo(activity)
        }
        Image(
            painter = painterResource(android.R.drawable.ic_menu_gallery),
            contentDescription = null,
            modifier = Modifier.size(80.dp)
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeWeatherInfo(activity: MainActivity) {

    val currentTime = remember { mutableStateOf(java.time.LocalDateTime.now()) }
    val date = currentTime.value.toLocalDate().toString()
    val time = currentTime.value.toLocalTime().withSecond(0).withNano(0).toString()
    val viewModel: WeatherViewModel = viewModel()


    LaunchedEffect(Unit) {
        WebSocketClient.connect(viewModel, activity)
    }
    val weatherInfoState = viewModel.messageLive.observeAsState()
    val weatherInfo = weatherInfoState.value;

    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(date, style = MaterialTheme.typography.titleMedium)
        Text(time, style = MaterialTheme.typography.titleSmall)
        Spacer(modifier = Modifier.height(8.dp))
        Text(weatherInfo?.getString("image")?:"", fontSize = 42.sp)
        Text(weatherInfo?.getString("weather")?:"", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(weatherInfo?.getString("message")?:"", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Alert ON", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
        Text("Air Quality Alert: ON", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
    }
}
