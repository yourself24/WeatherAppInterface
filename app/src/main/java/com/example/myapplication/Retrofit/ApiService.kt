package com.example.myapplication.Retrofit

import android.provider.ContactsContract.CommonDataKinds.Email
import com.example.myapplication.Models.LoginUser
import com.example.myapplication.Models.UpdatedUser
import com.example.myapplication.Models.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @GET("api/users/{id}")
    suspend fun getUser(@Path("id") userId: String): User
    @POST("api/users/login")
    fun loginUser(@Body loginUser: LoginUser): Call<Map<String, Any>>


    @GET("api/users/getEmail/{email}")
    suspend fun getUserByEmail(@Path("email") email: String): User
    @POST("api/users/create")
     suspend fun createUser(@Body user: User): User

     @GET("api/users/all")
     suspend fun getAllUsers():List<User>

     @POST("api/users/update/{id}")
     suspend fun updateUser(@Path("id")id:Long, @Body user:UpdatedUser): User

     @POST("api/users/forgot-password/{email}")
     suspend fun forgotPassword(@Path("email")email: String)

    // Define other API endpoints here
}
