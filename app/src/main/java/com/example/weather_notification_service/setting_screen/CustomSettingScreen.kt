package com.example.weather_notification_service.setting_screen

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Masks
import androidx.compose.material.icons.filled.Umbrella
import androidx.compose.material.icons.filled.Weekend
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import android.widget.NumberPicker
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.weather_notification_service.connection.RetrofitClient
import com.example.weather_notification_service.domain.CustomSettingEntity
import com.example.weather_notification_service.domain.dto.NotificationTimeRequest
import kotlinx.coroutines.launch

val memberId = "hello"

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NotificationSettingsScreen() {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val sharedPref = context.getSharedPreferences("alert_setting", Context.MODE_PRIVATE)

    var rainAlert by remember { mutableStateOf(true) }
    var dustAlert by remember { mutableStateOf(true) }
    var tempAlert by remember { mutableStateOf(true) }
    var weekendOff by remember { mutableStateOf(false) }
    var selectedHour by remember { mutableStateOf(8) }
    var selectedMinute by remember { mutableStateOf(0) }
    var alertTimes by remember { mutableStateOf(listOf<String>()) }

    LaunchedEffect(Unit) {
        try {
            val response = RetrofitClient.apiService.getCustomSettings(memberId)
            if (response.isSuccessful) {
                response.body() ?.let{ setting ->
                    rainAlert = setting.rain
                    dustAlert = setting.dust
                    tempAlert = setting.temp
                }

            } else {
                Toast.makeText(context, "서버 오류", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(context, "네트워크 오류", Toast.LENGTH_SHORT).show()
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        SettingRow(
            label = "Get alerts for rain",
            icon = Icons.Default.Umbrella,
            checked = rainAlert
        ) { newValue ->
            rainAlert = newValue
            with(sharedPref.edit()){
                putBoolean("rain_alert", newValue);
                apply()
            }
            coroutineScope.launch {
                try {
                    // 예시용 API (적절히 수정 필요)
                    val response = RetrofitClient.apiService.saveCustomSettings(CustomSettingEntity(memberId, "RAIN", newValue))
                    if (response.isSuccessful) {
                        Toast.makeText(context, "Rain alert 설정이 변경되었습니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "서버 오류", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Log.e("shiftrow", "네트워크 오류", e)
                    Toast.makeText(context, "네트워크 오류", Toast.LENGTH_SHORT).show()
                }
            }
        }
        SettingRow(
            label = "Get alerts for poor air quality",
            icon = Icons.Default.Masks,
            checked = dustAlert
        ) { newValue ->
            dustAlert = newValue
            with(sharedPref.edit()){
                putBoolean("dust_alert", newValue);
                apply()
            }
            coroutineScope.launch {
                try {
                    // 예시용 API (적절히 수정 필요)
                    val response = RetrofitClient.apiService.saveCustomSettings(CustomSettingEntity(memberId, "DUST", newValue))
                    if (response.isSuccessful) {
                        Toast.makeText(context, "air quality 설정이 변경되었습니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "서버 오류", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Log.e("shiftrow", "네트워크 오류", e)
                    Toast.makeText(context, "네트워크 오류", Toast.LENGTH_SHORT).show()
                }
            }
        }
        SettingRow(
            label = "Get clothing alerts for low temp",
            icon = Icons.Default.AcUnit,
            checked = tempAlert
        ) { newValue ->
            tempAlert = newValue
            with(sharedPref.edit()){
                putBoolean("temp_alert", newValue);
                apply()
            }
            coroutineScope.launch {
                try {
                    // 예시용 API (적절히 수정 필요)
                    val response = RetrofitClient.apiService.saveCustomSettings(CustomSettingEntity(memberId, "TEMP", newValue))
                    if (response.isSuccessful) {
                        Toast.makeText(context, "온도 설정이 변경되었습니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "서버 오류", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Log.e("shiftrow", "네트워크 오류", e)
                    Toast.makeText(context, "네트워크 오류", Toast.LENGTH_SHORT).show()
                }
            }
        }
        SettingRow(
            label = "repeat the alarm",
            icon = Icons.Default.Weekend,
            checked = weekendOff,
            onToggle = { weekendOff = it }
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors()
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Schedule,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text("Select alert times")
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    WheelNumberPicker(value = selectedHour, range = 0..23, onValueChange = { selectedHour = it })
                    WheelNumberPicker(value = selectedMinute, range = 0..59, onValueChange = { selectedMinute = it })
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(String.format("%02d:%02d", selectedHour, selectedMinute))
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        val time = String.format("%02d:%02d", selectedHour, selectedMinute)
                        if (!alertTimes.contains(time)) {
                            alertTimes = alertTimes + time
                        }
                    }) {
                        Text("Add")
                    }
                }
                FlowRow(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    alertTimes.sorted().forEach { time ->
                        Card(
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                            colors = CardDefaults.outlinedCardColors(),
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                            ) {
                                Text(time)
                                IconButton(onClick = { alertTimes = alertTimes - time }) {
                                    Icon(Icons.Default.Close, contentDescription = "Remove")
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        coroutineScope.launch {
                            try {
                                val request = NotificationTimeRequest(memberId, alertTimes)
                                val response = RetrofitClient.apiService.saveNotificationTime(request)
                                if (response.isSuccessful) {
                                    Toast.makeText(context, "시간 설정이 저장되었습니다", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "서버 오류", Toast.LENGTH_SHORT).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(context, "네트워크 오류", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Save")
                }
            }
        }
    }
}

@Composable
fun SettingRow(label: String, icon: ImageVector, checked: Boolean, onToggle: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
        Text(label)
        Spacer(Modifier.weight(1f))
        Switch(checked = checked, onCheckedChange = onToggle)
    }
}

@Composable
fun WheelNumberPicker(value: Int, range: IntRange, onValueChange: (Int) -> Unit, modifier: Modifier = Modifier) {
    AndroidView(
        factory = { context ->
            NumberPicker(context).apply {
                minValue = range.first
                maxValue = range.last
                this.value = value
                setOnValueChangedListener { _, _, newVal ->
                    onValueChange(newVal)
                }
            }
        },
        update = { picker -> picker.value = value },
        modifier = modifier
    )
}



