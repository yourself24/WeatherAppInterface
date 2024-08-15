package com.example.myapplication.Dashboards

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.myapplication.Models.WeatherProviderData
import com.example.myapplication.Retrofit.RetrofitClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch

@SuppressLint("MissingPermission")
@Composable
fun UserDashboard(navController: NavController) {
    var weatherDataList by remember { mutableStateOf<List<WeatherProviderData>>(emptyList()) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val fusedLocationClient: FusedLocationProviderClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    var userLocation by remember { mutableStateOf<Location?>(null) }

    LaunchedEffect(Unit) {
        // Check for location permissions
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            // Request location
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    userLocation = location

                    scope.launch {
                        try {
                            // Use the obtained location in your API requests
                            val latitude = location.latitude
                            val longitude = location.longitude

                            val vcData = RetrofitClient.getVCAPI().getVCData(latitude, longitude)
                            val wapiData = RetrofitClient.getVCAPI().getWeatherAPIData(latitude, longitude)
                            val tioData = RetrofitClient.getVCAPI().getTIOData(latitude, longitude)
                            val wbData = RetrofitClient.getVCAPI().getWeatherBit(latitude, longitude)
                            val wmData = RetrofitClient.getVCAPI().getWMData(latitude, longitude)

                            val weather1 = WeatherProviderData(
                                "VCData", vcData.address, vcData.description,
                                vcData.temperature, vcData.feelsLike, vcData.precip, vcData.humidity, vcData.pressure
                            )
                            val weather2 = WeatherProviderData(
                                "WeatherAPI", wapiData.address, wapiData.description,
                                wapiData.temperature, wapiData.feelsLike, 0.3, 80, wapiData.pressure
                            )
                            val weather3 = WeatherProviderData(
                                "TomorrowIO", tioData.address, tioData.description,
                                tioData.temperature, tioData.feelsLike, tioData.precip, tioData.humidity, tioData.pressure
                            )
                            val weather4 = WeatherProviderData(
                                "WeatherBit", wbData.address, wbData.description,
                                wbData.temperature, wbData.feelsLike, wbData.precip, wbData.humidity, wbData.pressure
                            )
                            val weather5 = WeatherProviderData(
                                "WeatherMap", wmData.address, wmData.description,
                                wmData.temperature, wmData.feelsLike, wmData.precip, wmData.humidity, wmData.pressure
                            )

                            weatherDataList = listOf(weather1, weather2, weather3, weather4, weather5)
                        } catch (e: Exception) {
                            Toast.makeText(context, "Failed to load weather data: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                } else {
                    Toast.makeText(context, "Unable to retrieve location", Toast.LENGTH_LONG).show()
                }
            }.addOnFailureListener { e ->
                Toast.makeText(context, "Failed to get location: ${e.message}", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(context, "Location permissions are required to use this feature.", Toast.LENGTH_LONG).show()
        }
    }

    WeatherDashboard(weatherList = weatherDataList)
}
