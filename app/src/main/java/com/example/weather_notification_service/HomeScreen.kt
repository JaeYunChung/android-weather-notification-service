package com.example.weather_notification_service

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.json.JSONObject
import java.util.Date

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(activity: MainActivity) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Image(painter = painterResource(android.R.drawable.ic_menu_compass), contentDescription = null, modifier = Modifier.size(80.dp))
        HomeWeatherInfo(activity)
        Image(painter = painterResource(android.R.drawable.ic_menu_gallery), contentDescription = null, modifier = Modifier.size(80.dp))
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
    val weatherInfo = weatherInfoState.value

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(date, fontSize = 20.sp)
        Text(time, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text("${weatherInfo?.getString("image")}", fontSize = 42.sp)
        Text("${weatherInfo?.getString("weather")}", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text("${weatherInfo?.getString("message")}", fontSize = 16.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Alert ON", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
        Text("Air Quality Alert: ON", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
    }
}
