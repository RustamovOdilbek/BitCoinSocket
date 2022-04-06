package com.example.socketexample.manager

import android.util.Log
import com.example.socketexample.SocketListener
import com.example.socketexample.model.BitCoin
import com.example.socketexample.model.Currency
import com.example.socketexample.model.DataSend
import com.google.gson.Gson
import okhttp3.*
import okio.ByteString

class SocketManager {

    lateinit var mwebSocket: WebSocket
    lateinit var socketListener: SocketListener
    private var gson = Gson()

    fun connectToSocket(s: String) {
        val client = OkHttpClient()

        val request: Request = Request.Builder().url("wss://ws.bitstamp.net").build()
        client.newWebSocket(request,object :WebSocketListener(){
            override fun onOpen(webSocket: WebSocket, response: Response) {
                mwebSocket = webSocket

                webSocket.send(gson.toJson(Currency("bts:subscribe",DataSend("live_trades_btcusd"))))

                /*webSocket.send("{\n" +
                        "    \"event\": \"bts:subscribe\",\n" +
                        "    \"data\": {\n" +
                        "        \"channel\": \"live_trades_btcusd\"\n" +
                        "    }\n" +
                        "}")*/
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d("@@@", "onMessage: $text")
                val bitCoin = gson.fromJson(text, BitCoin::class.java)
                socketListener.onSuccess(bitCoin)
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                Log.d("@@@", "onMessage bytes: $bytes")
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                Log.d("@@@", "onClosing: $code / $reason")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.d("@@@", "onFailure: ${t.message}")
                socketListener.onError(t.localizedMessage)
            }
        })
        client.dispatcher().executorService().shutdown()
    }

    fun socketListener(socketListener: SocketListener) {
        this.socketListener = socketListener
    }

}