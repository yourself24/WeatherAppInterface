package com.example.myapplication.Dashboards

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.Models.WeatherProviderData
import com.example.myapplication.R

@Composable
fun PodiumItem(rank: Int, providerScore: ProviderScore) {
    val iconResId = when (rank) {
        1 -> R.drawable.gold
        2 -> R.drawable.silver
        3 -> R.drawable.bronze
        else -> null
    }

    //so it's image-> provider name
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (iconResId != null) {
                Image(
                    painter = painterResource(id = iconResId),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = "#$rank ${providerScore.providerName}",
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp,
                fontFamily = MaterialTheme.typography.body1.fontFamily
            )
        }
        //display the categories that the provider won at
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "${providerScore.points} Points",
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp
            )
            Text(
                text = "Best at: ${providerScore.bestAt.joinToString(", ")}",
                fontWeight = FontWeight.Light,
                fontSize = 14.sp,
                fontFamily = MaterialTheme.typography.body2.fontFamily,
                color = Color.Gray
            )
        }
    }
}
data class ProviderScore(
    val providerName: String,
    var points: Int = 0,
    var bestAt: MutableList<String> = mutableListOf()
)

//to calcuate the score simply take the ones closest to the Arduino's vallues:
// i.e. smallest abs(provider-arduino)
fun calculateProviderScores(weatherDataList: List<WeatherProviderData>): List<ProviderScore> {
    val arduinoData = weatherDataList.find { it.providerName == "Arduino" }
    val otherProviders = weatherDataList.filter { it.providerName != "Arduino" }

    if (arduinoData == null) return emptyList()

    val providerScores = otherProviders.map { ProviderScore(it.providerName) }.toMutableList()

    //points are assgined to the ones with the smallest overall difference per category.
    fun assignPointBasedOnSmallestDifference(categoryName: String, arduinoValue: Double, selector: (WeatherProviderData) -> Double) {
        var smallestDifference = Double.MAX_VALUE
        var bestProvider: ProviderScore? = null

        for (provider in providerScores) {
            val providerData = weatherDataList.find { it.providerName == provider.providerName } ?: continue
            val difference = Math.abs(selector(providerData) - arduinoValue)
            Log.d("ScoreCalculation", "Category: $categoryName, Provider: ${provider.providerName}, Difference: $difference")
            if (difference < smallestDifference) {
                smallestDifference = difference
                bestProvider = provider
            }
        }

        bestProvider?.let {
            it.points++
            it.bestAt.add(categoryName)
            Log.d("ScoreCalculation", "Awarding point to ${it.providerName} for $categoryName")
        }
    }

    assignPointBasedOnSmallestDifference("Feels Like", arduinoData.feelsLike) { it.feelsLike }
    assignPointBasedOnSmallestDifference("Temperature", arduinoData.temperature) { it.temperature }
    assignPointBasedOnSmallestDifference("Humidity", arduinoData.humidity.toDouble()) { it.humidity.toDouble() }
    assignPointBasedOnSmallestDifference("Pressure", arduinoData.pressure) { it.pressure }
    providerScores.forEach{
        Log.d("FinalScores", "${it.providerName}: ${it.points} Points, Best At: ${it.bestAt.joinToString(", ")}")
    }
    return providerScores
}

@Composable
fun BestProviderView( weatherDataList: List<WeatherProviderData>) {
    val providerScores = remember { calculateProviderScores(weatherDataList) }
    val sortedProviderScores = providerScores.sortedByDescending { it.points }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Provider Standings",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            fontFamily = MaterialTheme.typography.h3.fontFamily,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        sortedProviderScores.forEachIndexed { index, providerScore ->
            PodiumItem(rank = index + 1, providerScore = providerScore)
        }
    }
}
@Composable
fun PodiumItem(rank: Int, providerScore: ProviderScore, weatherDataList: List<WeatherProviderData>) {
    val iconResId = when (rank) {
        1 -> R.drawable.gold
        2 -> R.drawable.silver
        3 -> R.drawable.bronze
        else -> null
    }

    val bestAt = calculateBestAt(providerScore.providerName, weatherDataList)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (iconResId != null) {
                Image(
                    painter = painterResource(id = iconResId),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = "#$rank ${providerScore.providerName}",
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp,
                fontFamily = MaterialTheme.typography.body1.fontFamily
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "${providerScore.points} Points",
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp
            )
            Text(
                text = "Best at: $bestAt",
                fontWeight = FontWeight.Light,
                fontSize = 14.sp,
                fontFamily = MaterialTheme.typography.body2.fontFamily,
                color = Color.Gray
            )
        }
    }
}

fun calculateBestAt(providerName: String, weatherDataList: List<WeatherProviderData>): String {
    val arduinoData = weatherDataList.find { it.providerName == "Arduino" } ?: return "N/A"
    val providerData = weatherDataList.find { it.providerName == providerName } ?: return "N/A"

    val bestAtList = mutableListOf<String>()

    if (Math.abs(providerData.feelsLike - arduinoData.feelsLike) < 1.0) {
        bestAtList.add("Feels Like")
    }
    if (Math.abs(providerData.temperature - arduinoData.temperature) < 1.0) {
        bestAtList.add("Temperature")
    }
    if (Math.abs(providerData.humidity - arduinoData.humidity) < 5) {
        bestAtList.add("Humidity")
    }
    if (Math.abs(providerData.pressure - arduinoData.pressure) < 5) {
        bestAtList.add("Pressure")
    }

    return if (bestAtList.isEmpty()) "None" else bestAtList.joinToString(", ")
}


