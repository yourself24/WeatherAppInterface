package com.example.myapplication.Retrofit

import com.example.myapplication.Models.User
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("api/users/{id}")
    suspend fun getUser(@Path("id") userId: String): User

    // Define other API endpoints here
}
