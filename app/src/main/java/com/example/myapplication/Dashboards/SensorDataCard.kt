package com.example.myapplication.Dashboards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.Models.SensorData

@Composable
fun SensorDataCard(sensorData: SensorData) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Display sensor data
            Text("Humidity: ${sensorData.humidity}%", style = MaterialTheme.typography.bodyMedium)
            Text("Temperature: ${sensorData.temperature1}°C", style = MaterialTheme.typography.bodyMedium)
            Text("Feels Like: ${sensorData.temperature2}°C", style = MaterialTheme.typography.bodyMedium)
            Text("Pressure: ${sensorData.pressure} hPa", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
