package com.example.socketexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import com.example.socketexample.databinding.ActivityMainBinding

import com.example.socketexample.manager.SocketManager

import com.example.socketexample.model.BitCoin

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var socketManager: SocketManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        socketManager = SocketManager()

        setupUI()

    }

    private fun setupUI() {
        binding.connect.setOnClickListener {
            socketManager.connectToSocket()
        }

        socketManager.socketListener(object : SocketListener {
            override fun onSuccess(bitCoin: BitCoin) {
                runOnUiThread {
                    if (bitCoin.event == "bts:subscription_succeeded") {
                        binding.connect.text = "Successfully Connected, Wait a moment"
                    } else {
                        binding.btnBtc.text = "1 BTC"
                        Log.d("@@@", "${bitCoin.data.price_str}")
                        binding.btnUsd.text = "${bitCoin.data.price_str}"
                    }
                }
            }

            override fun onError(message: String) {
                TODO("Not yet implemented")
            }

        })
    }
}