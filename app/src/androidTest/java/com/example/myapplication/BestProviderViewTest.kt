package com.example.myapplication

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.myapplication.Dashboards.BestProviderView
import com.example.myapplication.Models.WeatherProviderData
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BestProviderViewTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun bestProviderView_displaysProviderStandings() {

        val sampleWeatherData = listOf(
            WeatherProviderData(
                "Arduino", "123 Sup St", "Sunny", 25.0, 27.0, 10.0, 60, 1012.0
            ),
            WeatherProviderData(
                "Provider A", "456 Jon St", "Cloudy", 24.0, 26.0, 5.0, 62, 1011.0
            ),
            WeatherProviderData(
                "Provider B", "789 Hatz St", "Rainy", 23.0, 25.0, 15.0, 70, 1010.0
            )
        )


        composeTestRule.setContent {
            BestProviderView(weatherDataList = sampleWeatherData)
        }
        composeTestRule.onNodeWithText("Provider Standings").assertIsDisplayed()
        composeTestRule.onNodeWithText("#1 Provider A").assertIsDisplayed()
        composeTestRule.onNodeWithText("#2 Provider B").assertIsDisplayed()


        composeTestRule.onNodeWithText("1 Points").assertIsDisplayed()
        composeTestRule.onNodeWithText("Best at: Feels Like, Temperature, Humidity, Pressure").assertIsDisplayed()


        composeTestRule.onNodeWithText("#3 Arduino").assertIsDisplayed()
    }
}
