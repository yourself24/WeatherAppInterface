package com.example.myapplication.Dashboards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.Models.SensorData
import com.example.myapplication.Models.readSensorDataFromFile

@Composable
fun SensorDashboard(navController: NavController) {
    val context = LocalContext.current
    var sensorData by remember { mutableStateOf<SensorData?>(null) }

    LaunchedEffect(Unit) {
        sensorData = readSensorDataFromFile(context)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        sensorData?.let {
            SensorDataCard(sensorData = it)
        } ?: run {
            Text("No data available", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
