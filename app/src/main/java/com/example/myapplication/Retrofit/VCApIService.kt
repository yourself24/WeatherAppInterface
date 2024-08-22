package com.example.myapplication.Retrofit

import com.example.myapplication.Models.VCWeather
import com.example.myapplication.Models.WeatherProviderData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface VCApIService {

    @GET("/api/weather/VCdata/current/{latitude}/{longitude}")
    suspend fun getVCData(@Path("latitude") latitude: Double,@Path("longitude") longitude: Double): VCWeather
    @GET("/api/weather/WeatherAPIData/current/{latitude}/{longitude}")
    suspend fun getWeatherAPIData(@Path("latitude") latitude: Double,@Path("longitude") longitude: Double): VCWeather
    @GET("/api/weather/WeatherBitData/current/{latitude}/{longitude}")
    suspend fun getWeatherBit(@Path("latitude") latitude: Double,@Path("longitude") longitude: Double): VCWeather

    @GET("/api/weather/WMData/current/{latitude}/{longitude}")
    suspend fun getWMData(@Path("latitude") latitude: Double,@Path("longitude") longitude: Double): VCWeather
    @GET("/api/weather/TIOData/current/{latitude}/{longitude}")
    suspend fun getTIOData(@Path("latitude") latitude: Double,@Path("longitude") longitude: Double): VCWeather
    @GET("api/weather/LocationData/current/{longitude}/{latitude}")
    suspend fun getExactLocation(@Path("longitude") longitude: Double,@Path("latitude") latitude: Double): String
}