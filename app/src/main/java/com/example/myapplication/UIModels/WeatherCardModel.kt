package com.example.myapplication.UIModels

import android.icu.util.Calendar
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.example.myapplication.Models.WeatherProviderData
import com.example.myapplication.R

@Composable
fun WeatherCardModel(weatherData: WeatherProviderData) {
    val backgroundColor = WeatherBackground(weatherData)
    val weatherIcon = WeatherIcon(weatherData)
    val boxesColor = getContrastingColor(backgroundColor)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top section with icon, temperature, and address
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                // Weather Icon
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(weatherIcon) // Use the local drawable resource
                        .decoderFactory(GifDecoder.Factory())
                        .size(Size.ORIGINAL)
                        .build(),
                    contentDescription = "Seasonal Animation",
                    contentScale = ContentScale.Fit, // Adjust this to Fit or Crop based on how you want it displayed
                    modifier = Modifier.size(128.dp) // Adjust the size as needed
                )

                // Temperature
                Text(
                    text = "${weatherData.temperature}°C",
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontSize = 64.sp,
                        color = White
                    ),
                    modifier = Modifier.padding(top = 16.dp)
                )

                // Address
                Text(
                    text = weatherData.address,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = White.copy(alpha = 0.8f)
                    ),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // Bottom section with weather details
            OutlinedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f),
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(containerColor = getContrastingColor(backgroundColor)) // Darker color for contrast
            ) {
                // WeatherDetailBoxes laid out evenly
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        WeatherDetailBox("Feels Like", "${weatherData.feelsLike}", R.drawable.feelslike,boxesColor)
                        WeatherDetailBox("Rain", "${weatherData.precip}%", R.drawable.precipitation,boxesColor)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        WeatherDetailBox("Pressure", "${weatherData.pressure} hPa", R.drawable.pressure,boxesColor)
                        WeatherDetailBox("Humidity", "${weatherData.humidity}%", R.drawable.humidity,boxesColor)
                    }
                }
            }
        }
    }
}

@Composable
fun WeatherDetailBox(label: String, value: String, iconResource: Int,color:Color) {
    Box(

        modifier = Modifier
            .size(120.dp) // Increased size for better visibility
            .clip(RoundedCornerShape(16.dp))
            .background(color) // Consistent dark color for boxes
            .padding(12.dp)
            .border(
                BorderStroke(1.dp,Color.Black)
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
        ) {
            // Display Icon
            Image(
                painter = painterResource(id = iconResource),
                contentDescription = "$label Icon",
                modifier = Modifier.size(40.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = value,
                color = White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = label,
                color = White.copy(alpha = 0.7f),
                fontSize = 14.sp
            )
        }
    }
}

// Function to calculate a contrasting color based on the background color
fun getContrastingColor(color: Color): Color {
    // Determine if the background color is light or dark
    if (color==Color(0xFF2196F3)) {
      return  Color(0xFF1b65ae) // Dark color for light backgrounds
    } else if (color == Color(0xFF34495E)){
       return  Color(0xFF34345e) // Light color for dark backgrounds
    }
    else if(color == Color(0xFF4A6572)){
        return Color(0xFF4a5172)
    }
    else{
        return Color(0xFF081034)
    }

}

@Composable
fun WeatherIcon(weatherData: WeatherProviderData): Int {
    val now = Calendar.getInstance()
    val hour = now.get(Calendar.HOUR_OF_DAY)
    val isDayTime = hour in 6..18
    return when {
        weatherData.precip > 0.0 && weatherData.humidity > 70 && isDayTime-> R.drawable.raingood
        weatherData.precip > 0.0 && weatherData.humidity > 70 && !isDayTime-> R.drawable.nightrain// Replace with your rainy icon resource
        weatherData.humidity < 70 && weatherData.precip < 0.1 && isDayTime -> R.drawable.sungood
        weatherData.humidity < 70 && weatherData.precip < 0.1 && !isDayTime-> R.drawable.nightclear// Replace with your sunny icon resource
        else -> R.drawable.sunny // Replace with your default icon resource
    }
}

@Composable
fun WeatherBackground(weatherData: WeatherProviderData): Color {
    val now = Calendar.getInstance()
    val hour = now.get(Calendar.HOUR_OF_DAY)
    val isDayTime = hour in 6..18

    return when {
        weatherData.precip > 0.0 && weatherData.humidity > 70 && isDayTime -> Color(0xFF34495E) // Rainy day
        weatherData.precip > 0.0 && weatherData.humidity > 70 && !isDayTime -> Color(0xFF082634) // Rainy night
        weatherData.humidity < 70 && weatherData.precip < 0.1 && !isDayTime -> Color(0xFF4A6572) // Clear night
        weatherData.humidity < 70 && weatherData.precip < 0.1 && isDayTime -> Color(0xFF2196F3) // Sunny day
        else -> Color(0xFF2196F3) // Default to sunny day
    }
}


