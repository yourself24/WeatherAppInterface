package com.example.myapplication.Dashboards

import android.graphics.Color
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.myapplication.Models.WeatherProviderData
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState

@OptIn(ExperimentalPagerApi::class)
@Composable
fun GraphView(weatherDataList: List<WeatherProviderData>) {
    val pagerState = rememberPagerState()

    HorizontalPager(
        count = 5, // Number of graphs
        state = pagerState,
        modifier = Modifier.fillMaxSize()
    ) { page ->
        when (page) {
            0 -> WeatherBarChart(
                data = weatherDataList.map { it.temperature.toFloat() },
                labels = weatherDataList.map { it.providerName },
                label = "Temperature"
            )
            1 -> WeatherBarChart(
                data = weatherDataList.map { it.feelsLike.toFloat() },
                labels = weatherDataList.map { it.providerName },
                label = "Feels Like"
            )
            2 -> WeatherBarChart(
                data = weatherDataList.map { it.pressure.toFloat() },
                labels = weatherDataList.map { it.providerName },
                label = "Pressure"
            )
            3 -> WeatherBarChart(
                data = weatherDataList.map { it.humidity.toFloat() },
                labels = weatherDataList.map { it.providerName },
                label = "Humidity"
            )
            4 -> WeatherBarChart(
                data = weatherDataList.map { it.precip.toFloat() },
                labels = weatherDataList.map { it.providerName },
                label = "Precipitation"
            )
        }
    }
}
@Composable
fun WeatherBarChart(data: List<Float>, labels: List<String>, label: String) {
    AndroidView(
        factory = { context ->
            BarChart(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )

                // Styling the chart
                axisRight.isEnabled = false
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.setDrawGridLines(false)
                axisLeft.setDrawGridLines(true)
                axisLeft.granularity = 10f
                description.isEnabled = false

                // Setting up the legend
                legend.isEnabled = true
                legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
                legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
                legend.orientation = Legend.LegendOrientation.HORIZONTAL
                legend.setDrawInside(false)
                legend.textSize = 12f
                legend.form = Legend.LegendForm.CIRCLE
                legend.formSize = 10f
                legend.xEntrySpace = 20f // Increase space between legend entries
                legend.yEntrySpace = 15f // Increase space between lines of legend entries
                legend.formToTextSpace = 8f // Space between the form and the text

                // Setting up the data
                val entries = data.mapIndexed { index, value ->
                    BarEntry(index.toFloat(), value)
                }

                val dataSet = BarDataSet(entries, label).apply {
                    color = Color.BLUE
                    valueTextColor = Color.BLACK
                    valueTextSize = 12f
                    setDrawValues(true)
                }

                val barData = BarData(dataSet)
                barData.barWidth = 0.9f // set custom bar width
                this.data = barData
                xAxis.valueFormatter = IndexAxisValueFormatter(labels)
                xAxis.setLabelCount(labels.size, false)
                xAxis.textSize = 12f
                xAxis.textColor = Color.BLACK

                // Refresh the chart
                invalidate()
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    )
}

