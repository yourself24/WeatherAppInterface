package com.example.myapplication.Retrofit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

//    private static final String BASE_URL = "http://ec2-13-60-214-97.eu-north-1.compute.amazonaws.com:8080/"; // AWS server IP 
    private static final String BASE_URL = "http://192.168.0.120:8080/"; // Replace when trying to test locally(ipconfig to get IP
    //MUST BE ON SAME NETWORK!!!!)

    private static Retrofit retrofit = null;

    // Method to create an OkHttpClient with the CookieJar
    private static OkHttpClient getOkHttpClient() {
        return new OkHttpClient.Builder()
                .cookieJar(new SessionCookieJar()) // Attach the custom CookieJar
                .build();
    }

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(getOkHttpClient()) // Use the custom OkHttpClient
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static ApiService getApiService() {
        return getClient().create(ApiService.class);
    }

    public static VCApIService getVCAPI() {
        return getClient().create(VCApIService.class);
    }
}
