package com.example.myapplication.Dashboards

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.example.myapplication.Models.WeatherProviderData
import com.example.myapplication.Retrofit.RetrofitClient
import com.example.myapplication.UIModels.WeatherCardModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@SuppressLint("NewApi")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@Composable
fun UserWeatherView(email: String) {

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val dataList = remember { mutableStateListOf<Pair<LocalDateTime, WeatherProviderData>>() }
    val pagerState = rememberPagerState()
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var selectedHour by remember { mutableStateOf<LocalTime?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")

    LaunchedEffect(email) {
        scope.launch {
            try {

                //gather all saved weather data by user
                val userId = RetrofitClient.getApiService().getUserByEmail(email).id
                val fetchedDataList = RetrofitClient.getVCAPI().getDataByUID(userId)
                fetchedDataList.forEach { elem ->
                    //go through each and construct a WEatherProviderData object that we can display
                    val weatherData = WeatherProviderData().apply {
                        providerName = elem.provider
                        address = elem.address
                        description = "N/A"
                        temperature = elem.temperature
                        feelsLike = elem.feels
                        precip = elem.precipitation
                        humidity = elem.humidity
                        pressure = elem.pressure
                    }
                    // parse the date so that we can use it for filtering later
                    val dateTime = LocalDateTime.parse(elem.date, dateFormatter)
                    dataList.add(dateTime to weatherData)
                }
                isLoading = false
            } catch (e: Exception) {
                Toast.makeText(
                    context, "Failed to load data because of: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
                isLoading = false
            }
        }
    }

    //get all dates in a list and all hours for that specific date in another list
    val uniqueDates = dataList.map { it.first.toLocalDate() }.distinct()
    val uniqueHours = selectedDate?.let {
        dataList.filter { it.first.toLocalDate() == selectedDate }
            .map { it.first.toLocalTime().withMinute(0).withSecond(0).withNano(0) }
            .distinct()
    } ?: emptyList()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        DateDropdownSelector(dates = uniqueDates, selectedDate = selectedDate) { newDate ->
            selectedDate = newDate
            selectedHour = null
        }
    //allow user to select hours only after selecting their date
        if (selectedDate != null) {
            Spacer(modifier = Modifier.height(8.dp))
            HourDropdownSelector(hours = uniqueHours, selectedHour = selectedHour) { newHour ->
                selectedHour = newHour
            }
        }
        //display a loading indicator while data is loading
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            //list of filtered data for specific hour and day)
            val filteredList = dataList.filter {
                (selectedDate == null || it.first.toLocalDate() == selectedDate) &&
                        (selectedHour == null || it.first.toLocalTime().withMinute(0).withSecond(0).withNano(0) == selectedHour)
            }.map { it.second }

            if (filteredList.isNotEmpty()) {
                HorizontalPager(
                    count = filteredList.size,
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    //display a weathercardmodel for each element in the list in a pager that user can swipe through 
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .background(MaterialTheme.colorScheme.surface, shape = MaterialTheme.shapes.medium)
                            .shadow(4.dp, shape = MaterialTheme.shapes.medium)
                            .padding(16.dp)
                    ) {
                        WeatherCardModel(weatherData = filteredList[page])
                    }
                }
            } else {
                Text(
                    text = "No weather data available",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateDropdownSelector(
    dates: List<LocalDate>,
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(selectedDate?.toString() ?: "Select a date") }

    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, shape = MaterialTheme.shapes.small)
            .shadow(2.dp, shape = MaterialTheme.shapes.small)
    ) {
        TextField(
            value = selectedText,
            onValueChange = {},
            readOnly = true,
            label = { Text("Date") },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            },
            colors = TextFieldDefaults.textFieldColors(
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurface
            )
        )

        if (expanded) {
            Popup(
                alignment = Alignment.TopStart,
                onDismissRequest = { expanded = false }
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .background(MaterialTheme.colorScheme.surface, shape = MaterialTheme.shapes.medium)
                        .shadow(4.dp, shape = MaterialTheme.shapes.medium)
                        .padding(8.dp),
                    shape = MaterialTheme.shapes.medium,
                ) {
                    Column {
                        dates.forEach { date ->
                            DropdownMenuItem(
                                text = { Text(text = date.toString(), color = MaterialTheme.colorScheme.onSurface) },
                                onClick = {
                                    selectedText = date.toString()
                                    onDateSelected(date)
                                    expanded = false
                                },
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HourDropdownSelector(
    hours: List<LocalTime>,
    selectedHour: LocalTime?,
    onHourSelected: (LocalTime) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(selectedHour?.toString() ?: "Select an hour") }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, shape = MaterialTheme.shapes.small)
            .shadow(2.dp, shape = MaterialTheme.shapes.small)
    ) {
        TextField(
            value = selectedText,
            onValueChange = {},
            readOnly = true,
            label = { Text("Hour") },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            },
            colors = TextFieldDefaults.textFieldColors(
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurface
            )
        )

        if (expanded) {
            Popup(
                alignment = Alignment.TopStart,
                onDismissRequest = { expanded = false }
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .background(MaterialTheme.colorScheme.surface, shape = MaterialTheme.shapes.medium)
                        .shadow(4.dp, shape = MaterialTheme.shapes.medium)
                        .padding(8.dp),
                    shape = MaterialTheme.shapes.medium,
                ) {
                    Column {
                        hours.forEach { hour ->
                            DropdownMenuItem(
                                text = { Text(text = hour.toString(), color = MaterialTheme.colorScheme.onSurface) },
                                onClick = {
                                    selectedText = hour.toString()
                                    onHourSelected(hour)
                                    expanded = false
                                },
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
