package com.example.lifecalculator

import com.google.gson.annotations.SerializedName
import java.io.Serial

data class CountryData(
    val id: Int,
    val country: String,
    val lifeBoth: Double,
    val lifeFemale: Double,
    val lifeMale: Double
)
