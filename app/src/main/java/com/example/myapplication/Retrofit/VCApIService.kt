package com.example.myapplication.Retrofit

import com.example.myapplication.Models.VCWeather
import retrofit2.http.GET

interface VCApIService {

    @GET("/api/VCdata/current")
    suspend fun getWeather(): VCWeather
}