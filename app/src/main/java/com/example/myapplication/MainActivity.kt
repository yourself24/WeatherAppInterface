package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.Models.User
import com.example.myapplication.Models.VCWeather
import com.example.myapplication.Retrofit.RetrofitClient
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyAppContent()
        }
    }

    @Composable
    fun MyAppContent() {
        var user by remember { mutableStateOf<User?>(null) }
        var showDetails by remember { mutableStateOf(false) }
        var showData by remember { mutableStateOf(false) }
        var weatherData by remember { mutableStateOf<VCWeather?>(null) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = { showData = true }) {
                Text(text = "View Weather Data")
            }

            // Display weather data when showData is true
            if (showData) {
                WeatherData(data = weatherData)
            }
        }

        LaunchedEffect(showData) {
            if (showData) {
                try {
                    // Fetch weather data from API
                    val fetchedWeather = fetchData()
                    weatherData = fetchedWeather
                } catch (e: Exception) {
                    // Handle error, e.g., show error message
                    weatherData = null
                }
            }
        }
    }

    @Composable
    fun Greeting(name: String) {
        Text(text = "Hello $name!")
    }

    @Composable
    fun UserDetails(user: User) {
        Column {
            Text(text = "Name: ${user.name}")
            Text(text = "Email: ${user.email}")
            Text(text = "Age: ${user.age}")
            Text(text = "Location: ${user.location}")

            Button(onClick = { /* Hide details */ }) {
                Text(text = "Hide Details")
            }
        }
    }

    @Composable
    fun WeatherData(data: VCWeather?) {
        data?.let {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Address: ${it.address}")
                Text(text = "Temperature: ${it.temperature}")
                Text(text = "Humidity: ${it.humidity}")
            }
        }
    }

    private suspend fun fetchUser(userId: String): User {
        return RetrofitClient.getApiService().getUser(userId)
    }

    private suspend fun fetchData(): VCWeather {
        return RetrofitClient.getVCAPI().getWeather()
    }
}
