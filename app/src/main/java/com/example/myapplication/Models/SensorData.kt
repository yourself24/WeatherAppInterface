package com.example.myapplication.Models

import android.content.Context
import java.io.File

data class SensorData(
    val humidity: Float,
    val temperature1: Float,
    val temperature2: Float,
    val pressure: Float
)

fun readSensorDataFromFile(context: Context): SensorData? {
    val fileName = "bluetooth_data.txt"
    val file = File(context.getExternalFilesDir(null), fileName)

    if (!file.exists()) return null

    val lines = file.readLines()
    var humidity = 0f
    var temperature1 = 0f
    var temperature2 = 0f
    var pressure = 0f

    for (i in lines.indices) {
        val line = lines[i]
        when {
            line.startsWith("Humidity:") -> humidity = line.removePrefix("Humidity:").toFloat()
            line.startsWith("Temperature1:") -> temperature1 = line.removePrefix("Temperature1:").toFloat()
            line.startsWith("Temperature2") -> {
                // Temperature2 might be split across lines
                val tempLine = line.removePrefix("Temperature2")
                temperature2 = if (tempLine.isNotBlank()) {
                    tempLine.toFloat()
                } else {
                    lines.getOrNull(i + 1)?.toFloat() ?: 0f
                }
            }
            line.startsWith("Pressure:") -> pressure = line.removePrefix("Pressure:").removeSuffix("Pa").toFloat()
        }
    }

    return SensorData(humidity, temperature1, temperature2, pressure)
}

