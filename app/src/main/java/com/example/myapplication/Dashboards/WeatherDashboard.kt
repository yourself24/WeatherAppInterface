package com.example.myapplication.Dashboards

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.Models.User
import com.example.myapplication.Models.WeatherProviderData
import com.example.myapplication.R
import com.example.myapplication.Retrofit.RetrofitClient
import com.example.myapplication.UIModels.WeatherCardModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun WeatherDashboard(
    bluetoothHandler: BluetoothHandler,
    navController: NavController,
    weatherList: List<WeatherProviderData>,
    email: String
) {
    val pagerState = rememberPagerState()
    var selectedView by remember { mutableStateOf("Weather View") } // Track selected view
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scaffoldState = rememberScaffoldState(drawerState = drawerState)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text(selectedView) },
                navigationIcon = {
                    IconButton(onClick = { scope.launch { drawerState.open() } }) {
                        Icon(
                            painter = painterResource(id = R.drawable.optionshuge),
                            contentDescription = "Menu",
                            tint = Color.Unspecified
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        scope.launch {
                            try {
                                val logoutSting = RetrofitClient.getApiService().logOut()
                                navController.navigate("login_screen")
                                Toast.makeText(context,logoutSting,
                                    Toast.LENGTH_LONG).show()
                            }catch (e:Exception){
                                navController.navigate("welcome_screen")
                                bluetoothHandler.closeConnection()
                                Toast.makeText(context,"Failed to load users because of: ${e.message}",
                                    Toast.LENGTH_LONG).show()

                            }

                        }


                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.logoutbig),
                            contentDescription = "Logout",
                            tint = Color.Unspecified
                        )
                    }
                },
                backgroundColor = MaterialTheme.colorScheme.primary
            )
        },
        drawerContent = {
            DrawerContent(
                selectedView = selectedView,
                onViewSelected = { view ->
                    selectedView = view
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)  // This is necessary to avoid overlapping the top bar
        ) {
            when (selectedView) {
                "Weather View" -> {
                    HorizontalPager(
                        count = weatherList.size,
                        state = pagerState,
                        modifier = Modifier.weight(1f)
                    ) { page ->
                        WeatherCardModel(weatherData = weatherList[page])
                    }

                    HorizontalPagerIndicator(
                        pagerState = pagerState,
                        activeColor = MaterialTheme.colorScheme.primary,
                        inactiveColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.3f)
                    )
                }

                "Graph View"->{

                    GraphView(weatherDataList = weatherList)
                }
                "User View"->{

                    UserEditView(navController = navController, email = email)



                }
                else -> {
                    BestProviderView(navController,weatherList)
                }
            }
        }
    }
}

@Composable
fun DrawerContent(selectedView: String, onViewSelected: (String) -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Navigation", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        DrawerItem("Weather View", selectedView, onViewSelected,R.drawable.weatherselect)
        DrawerItem("Graph View", selectedView, onViewSelected,R.drawable.statistics)
        DrawerItem("Best Provider", selectedView, onViewSelected,R.drawable.winning)
        DrawerItem("User View", selectedView, onViewSelected,R.drawable.winning)
    }
}

@Composable
fun DrawerItem(
    label: String,
    selectedView: String,
    onViewSelected: (String) -> Unit,
    icon: Int
) {
    TextButton(
        onClick = { onViewSelected(label) },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.textButtonColors(
            contentColor = if (selectedView == label) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp)
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = "$label icon",
                modifier = Modifier.size(24.dp),
                tint = Color.Unspecified // Ensure original colors are retained
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(label)
        }
    }
}

@Composable
fun PlaceholderView(viewName: String) {
    // Placeholder content for other views
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "$viewName content goes here", style = MaterialTheme.typography.bodyMedium)
    }
}
