package com.example.lifecalculator.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val api: ApiService by lazy{
        Retrofit.Builder()
            .baseUrl("https://1fe1-2a0b-6204-2eb2-6200-4830-799a-6905-fc2a.ngrok-free.app")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}