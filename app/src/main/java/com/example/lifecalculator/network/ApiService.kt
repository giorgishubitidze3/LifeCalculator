package com.example.lifecalculator.network

import com.example.lifecalculator.CountryData
import retrofit2.http.GET

interface ApiService {

    @GET("/api/data")
    suspend fun getCountryData(): List<CountryData>
}