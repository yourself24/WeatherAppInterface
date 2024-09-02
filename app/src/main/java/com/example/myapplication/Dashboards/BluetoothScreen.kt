package com.example.myapplication.Dashboards

import android.location.Location
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.Models.WeatherProviderData
import com.example.myapplication.UIModels.WeatherCardModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.regex.Pattern

@Composable
fun YourBluetoothScreen(navController: NavController) {
    val context = LocalContext.current
    var sensorData by remember { mutableStateOf("Waiting for data...") }
    val fusedLocationClient: FusedLocationProviderClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }
    val deviceAddress = "00:24:01:00:0A:DC" // Replace with MAC address accordingly, from mobile device settings

    // Initialize BluetoothHandler
    val bluetoothHandler = remember { BluetoothHandler(context, deviceAddress) }
    var userLocation by remember { mutableStateOf<Location?>(null) }

    var parsedData by remember { mutableStateOf<WeatherProviderData?>(null) }

    // Connect to the Bluetooth device
    LaunchedEffect(Unit) {
        if (bluetoothHandler.connect()) {
            bluetoothHandler.readData { data ->
                sensorData = data
                Log.d("RawData", data)  // Log the raw data for debugging
            }
        } else {
            sensorData = "Failed to connect"
        }
    }

    // Re-parse sensor data whenever sensorData changes
    LaunchedEffect(sensorData) {
        parsedData = parseSensorData(sensorData)
    }
    DisposableEffect(Unit) {
        onDispose {
            // Close connection when leaving screen as to allow user to use the offline screen after logging out
            bluetoothHandler.closeConnection() 
          
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (parsedData != null) {
            WeatherCardModel(parsedData!!)
        } else {
            Text(text = sensorData, modifier = Modifier.padding(16.dp))
        }

        Button(
            onClick = {
                bluetoothHandler.closeConnection()
                navController.navigateUp()
            },
            modifier = Modifier.padding(top = 20.dp)
        ) {
            Text("Disconnect")
        }
    }
}
//parse data received from arduino via the pattern used in the code and store it to form a WeatherProviderData object and display it
// 

fun parseSensorData(data: String): WeatherProviderData {
    val humidityPattern = "Humidity:".toRegex()
    val temperature1Pattern = "Temperature1:".toRegex()
    val temperature2Pattern = "Temperature2:".toRegex()
    val pressurePattern = "Pressure:".toRegex()

    val humidityMatch = humidityPattern.find(data)
    val temperature1Match = temperature1Pattern.find(data)
    val temperature2Match = temperature2Pattern.find(data)
    val pressureMatch = pressurePattern.find(data)

    if (humidityMatch != null && temperature1Match != null && temperature2Match != null && pressureMatch != null) {
        val humidity = data.substring(humidityMatch.range.last + 1, temperature1Match.range.first).trim().removeSuffix(",")
        val temperature1 = data.substring(temperature1Match.range.last + 1, temperature2Match.range.first).trim().removeSuffix(",")
        val temperature2 = data.substring(temperature2Match.range.last + 1, pressureMatch.range.first).trim().removeSuffix(",")
        val pressure = data.substring(pressureMatch.range.last + 1).trim().replace("Pa", "").removeSuffix(",")

        Log.d("current data",humidity+temperature1+temperature2+pressure)


        return WeatherProviderData("Arduino","Dej","N/A",temperature1.toDouble(),temperature2.toDouble(),0.0,humidity.toDouble().toInt(),pressure.toDouble())
    }

    return WeatherProviderData("NA","NA,","NA",0.0,0.0,0.0,0,0.0)
}

