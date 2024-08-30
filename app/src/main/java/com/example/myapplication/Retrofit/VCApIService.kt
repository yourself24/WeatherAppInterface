package com.example.myapplication.Retrofit

import com.example.myapplication.Models.VCWeather
import com.example.myapplication.Models.WeatherDataClass
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime

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
    @GET("api/weather/allData/get")
    suspend fun getAllData():List<WeatherDataClass>
    @GET("api/weather/allData/getDate/{date}")
    suspend fun getDataByDate(@Path("date")date:OffsetDateTime):List<WeatherDataClass>
    @POST("api/weather/allData/add")
    suspend fun addData(@Body data: WeatherDataClass): WeatherDataClass
    @POST("api/weather/allData/addFake")
    suspend fun addFake(@Body date: String): WeatherDataClass
    @GET("api/weather/allData/getID/{id}")
    suspend fun getDataByUID(@Path("id")id:Long):List<WeatherDataClass>
}