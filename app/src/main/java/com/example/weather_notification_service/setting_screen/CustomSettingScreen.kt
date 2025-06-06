package com.example.weather_notification_service.setting_screen

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.weather_notification_service.connection.RetrofitClient
import com.example.weather_notification_service.domain.CustomSettingEntity
import kotlinx.coroutines.launch

val memberId = "hello"

@Composable
fun NotificationSettingsScreen() {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val sharedPref = context.getSharedPreferences("alert_setting", Context.MODE_PRIVATE)

    var rainAlert by remember { mutableStateOf(sharedPref.getBoolean("rain_alert", false)) }
    var dustAlert by remember { mutableStateOf(sharedPref.getBoolean("dust_alert", false)) }
    var tempAlert by remember { mutableStateOf(sharedPref.getBoolean("temp_alert", false)) }
    var weekendOff by remember { mutableStateOf(sharedPref.getBoolean("week_alert", false)) }

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
        SettingRow("Get alerts for rain", rainAlert)  { newValue ->
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
        SettingRow("Get alerts for poor air quality", dustAlert)  { newValue ->
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
        SettingRow("Get clothing alerts for low temp", tempAlert)  { newValue ->
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



