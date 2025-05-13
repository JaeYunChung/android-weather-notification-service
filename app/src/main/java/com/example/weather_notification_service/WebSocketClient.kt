package com.example.weather_notification_service

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.ComponentActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONObject
import java.util.UUID

object WebSocketClient {
    private lateinit var webSocket: WebSocket
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var deviceId: String

    @RequiresApi(Build.VERSION_CODES.P)
    val handler = Handler.createAsync(Looper.getMainLooper())

    fun connect(viewModel: WeatherViewModel, @SuppressLint("RestrictedApi") activity: ComponentActivity?) {
        if (activity == null) {
            Log.e("WebSocketClient", "connect: activity is null")
            return
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)

        val client = OkHttpClient()
        val request = Request.Builder()
            .url("ws://10.0.2.2:8080/ws")
            .build()
        deviceId = UUID.randomUUID().toString()
        webSocket = client.newWebSocket(request, object : WebSocketListener() {

            @RequiresApi(Build.VERSION_CODES.P)
            override fun onOpen(ws: WebSocket, response: Response) {
                Log.d("WS", "Connected")
                startPeriodicRequest(activity)
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                val weatherInfo = JSONObject(text)
                viewModel.messageLive.postValue(weatherInfo)
            }

            override fun onFailure(ws: WebSocket, t: Throwable, response: Response?) {
                Log.e("WS", "Failure: ${t.message}")
                reconnect(viewModel, activity)
            }

            override fun onClosing(ws: WebSocket, code: Int, reason: String) {
                Log.d("WS", "Closing: $reason")
                ws.close(1000, null)
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun startPeriodicRequest(@SuppressLint("RestrictedApi") activity: ComponentActivity?) {
        if (activity == null) {
            Log.e("WebSocketClient", "startPeriodicRequest: activity is null")
            return
        }

        handler.post(object : Runnable {
            override fun run() {
                Log.d("WebSocket", "handler 루프 동작 중")
                if (ActivityCompat.checkSelfPermission(
                        activity,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(
                        activity,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    Log.d("Permisson", "permisson을 얻었습니다..")
                    ActivityCompat.requestPermissions(
                        activity,
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ), 1
                    )
                    return
                }

                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        if (location != null) {
                            val latitude = location.latitude
                            val longitude = location.longitude
                            val json = JSONObject()
                            json.put("deviceId", deviceId)
                            json.put("longitude", longitude)
                            json.put("latitude", latitude)
                            webSocket.send(json.toString())
                            Log.d("Location", "위도: $latitude, 경도: $longitude")
                        }
                        else{
                            requestCurrentLocation(activity)
                        }
                    }
                    .addOnFailureListener {
                        Log.e("LocationDebug", "location request failed: ${it.message}")
                    }

                handler.postDelayed(this, 5000)
            }
        })
    }

    private fun reconnect(viewModel: WeatherViewModel, @SuppressLint("RestrictedApi") activity: ComponentActivity?) {
        if (activity == null) {
            Log.e("WebSocketClient", "reconnect: activity is null")
            return
        }

        Handler(Looper.getMainLooper()).postDelayed({
            Log.d("WebSocket", "Reconnecting to server...")
            connect(viewModel, activity)
        }, 3000)
    }

    @SuppressLint("MissingPermission")
    fun requestCurrentLocation(context: Context) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        val locationRequest = com.google.android.gms.location.LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY, 10000
        ).build()

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val location = locationResult.lastLocation
                if (location != null) {
                    Log.d("LocationRequest", "위치 요청 성공: lat=${location.latitude}, lon=${location.longitude}")
                } else {
                    Log.w("LocationRequest", "위치 결과는 있지만 null임")
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

}
