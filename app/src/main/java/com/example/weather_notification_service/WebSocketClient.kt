package com.example.weather_notification_service

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONObject

object WebSocketClient {
    private  lateinit var webSocket: WebSocket

    fun connect(viewModel: WeatherViewModel){
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("ws://10.0.2.2:8080/")
            .build()
        webSocket = client.newWebSocket(request, object : WebSocketListener() {


            override fun onOpen(ws: WebSocket, response: Response) {
                Log.d("WS", "Connected")
            }

            override fun onMessage(webSocket: WebSocket, text:String) {
                super.onMessage(webSocket, text)
                val weatherInfo = JSONObject(text)
                viewModel.messageLive.postValue(weatherInfo)
            }

            override fun onFailure(ws: WebSocket, t: Throwable, response: Response?) {
                Log.e("WS", "Failure: ${t.message}")
                reconnect(viewModel)
            }

            override fun onClosing(ws: WebSocket, code: Int, reason: String) {
                Log.d("WS", "Closing: $reason")
                ws.close(1000, null)
            }
        })
    }

    private fun reconnect(viewModel: WeatherViewModel) {
        Handler(Looper.getMainLooper()).postDelayed({
            Log.d("WebSocket", "Reconnecting to server...")
            connect(viewModel)
        }, 3000)
    }

}