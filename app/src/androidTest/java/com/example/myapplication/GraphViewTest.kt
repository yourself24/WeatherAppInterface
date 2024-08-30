package com.example.myapplication

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.myapplication.Dashboards.GraphView
import com.example.myapplication.Models.WeatherProviderData
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GraphViewTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun graphView_displaysChartsCorrectly() {

        val sampleWeatherData = listOf(
            WeatherProviderData(
                "Provider A", "123 Main St", "Sunny", 25.0, 27.0, 10.0, 60, 1012.0
            ),
            WeatherProviderData(
                "Provider B", "456 Elm St", "Cloudy", 22.0, 23.0, 5.0, 65, 1010.0
            ),
        )


        composeTestRule.setContent {
            GraphView(weatherDataList = sampleWeatherData)
        }

        repeat(5) { page ->
            // Swipe to the next page (chart)
            if (page > 0) {
                composeTestRule.onRoot().performTouchInput {
                    swipeLeft()
                }
            }


            composeTestRule.waitForIdle()

            val expectedTitle = when (page) {
                0 -> "Temperature"
                1 -> "Feels Like"
                2 -> "Pressure"
                3 -> "Humidity"
                4 -> "Precipitation"
                else -> throw IllegalArgumentException("Unexpected page")
            }

            composeTestRule.onNodeWithText(expectedTitle).assertIsDisplayed()
        }
    }
    }

