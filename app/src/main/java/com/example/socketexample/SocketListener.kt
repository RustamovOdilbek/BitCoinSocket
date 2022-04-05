package com.example.socketexample

import com.example.socketexample.model.BitCoin

interface SocketListener {
    fun onSuccess(bitCoin: BitCoin)
    fun onError(message: String)
}