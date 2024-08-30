package com.example.myapplication

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.assertIsDisplayed
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.myapplication.Models.WeatherProviderData
import com.example.myapplication.UIModels.WeatherCardModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WeatherCardModelTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun weatherCardDisplaysCorrectly() {

        val weatherData = WeatherProviderData(
            "Hatz", "Test", "New York", 25.0, 27.0, 10.0, 80, 1012.0
        )


        composeTestRule.setContent {
            WeatherCardModel(weatherData = weatherData)
        }


        composeTestRule.onNodeWithText("27.0°C").assertIsDisplayed()


        composeTestRule.onNodeWithText("Feels Like").assertIsDisplayed()
        composeTestRule.onNodeWithText("27.0").assertIsDisplayed()


        composeTestRule.onNodeWithText("Rain").assertIsDisplayed()
        composeTestRule.onNodeWithText("10%").assertIsDisplayed()


        composeTestRule.onNodeWithText("Pressure").assertIsDisplayed()
        composeTestRule.onNodeWithText("1012.0 hPa").assertIsDisplayed()

        composeTestRule.onNodeWithText("Humidity").assertIsDisplayed()
        composeTestRule.onNodeWithText("80%").assertIsDisplayed()


        composeTestRule.onNodeWithContentDescription("Seasonal Animation").assertIsDisplayed()
    }
}
