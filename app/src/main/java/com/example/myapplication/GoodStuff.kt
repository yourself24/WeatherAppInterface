package com.example.myapplication//package com.example.myapplication
//
//import android.content.res.Configuration
//import android.content.res.Configuration.UI_MODE_NIGHT_YES
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.activity.enableEdgeToEdge
//import androidx.compose.animation.core.animateDpAsState
//import androidx.compose.animation.core.tween
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonColors
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.ElevatedButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.OutlinedButton
//import androidx.compose.material3.Surface
//import androidx.compose.material3.Text
//import androidx.compose.runtime.*
//import androidx.compose.runtime.saveable.rememberSaveable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.colorResource
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.tooling.preview.Wallpapers
//import androidx.compose.ui.unit.dp
//import com.example.myapplication.Models.User
//import com.example.myapplication.Models.VCWeather
//import com.example.myapplication.Retrofit.RetrofitClient
//import com.example.myapplication.ui.theme.MyApplicationTheme
//import kotlinx.coroutines.launch
//
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            MyApplicationTheme {
//                MyApp()
//            }
//        }
//    }
//}
//
//@Composable
//fun Greetings(names:List<String> = List(100){"$it"}) {
//    // A surface container using the 'background' color from the theme
//    Surface(color = MaterialTheme.colorScheme.background) {
//        LazyColumn {
//            item{Text("Header")}
//            items(names){
//                name-> Greeting(name)
//            }
//        }
//
//
//
//    }
//
//}
//
//@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    val expanded = remember {mutableStateOf(false) }
//    // using remember only calculates when whe recompose
//    val extraPadding by animateDpAsState(targetValue = if(expanded.value) 48.dp else 0.dp,
//        animationSpec = tween(
//            1500
//        ),
//        label = "Yes Sir"
//    )
//    Surface(color = MaterialTheme.colorScheme.primary,
//        modifier= Modifier.padding(horizontal = 8.dp,4.dp)) {
//        Row(modifier= Modifier.padding(24.dp)) {
//            Column(
//                modifier = Modifier
//
//                    .weight(0.2f)
//                    .padding(bottom = extraPadding)
//            ) {
//                Text(
//                    text = "Hello,"
//
//                )
//                Text(
//                    text = name
//
//                )
//            }
//            ElevatedButton(onClick = { expanded.value= !expanded.value}
//            , colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
//                ) {
//
//                Text(
//                    if (expanded.value) "Show More" else "Show More",
//
//                )
//            }
//        }
//    }
//
//}
//
//@Composable
//fun MyApp() {
//    var shouldShowOnboarding by rememberSaveable  { mutableStateOf(true) }
//    //rememberSaveable remembers the state even after configuration changes(i.e rotate or dark mode)
//    if(shouldShowOnboarding){
//        OnboardingScreen(onContinueClicked = {shouldShowOnboarding = false})
//    }
//    else{
//        Greetings()
//    }
//
//}
//
//
//@Composable
//fun OnboardingScreen(modifier: Modifier = Modifier,
//                     onContinueClicked: ()->Unit) {
//    // TODO: This state should be hoisted
//    //using var and by, then we can stop using .value from the remember statement
//
//    Column(
//        modifier = modifier.fillMaxSize(),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text("Welcome to the Basics Codelab!")
//        Button(
//            modifier = Modifier.padding(vertical = 24.dp),
//            onClick = onContinueClicked
//        ) {
//            Text("Continue")
//        }
//    }
//}
//@Preview(showBackground = true, widthDp = 320, heightDp = 320,uiMode = Configuration.UI_MODE_NIGHT_NO)
//@Preview(showBackground = true, widthDp = 320, heightDp = 320, uiMode = UI_MODE_NIGHT_YES )
//@Composable
//fun OnboardingPreview() {
//    MyApplicationTheme  {
//        OnboardingScreen(onContinueClicked = {})
//    }
//}
//
//
//
//@Preview(
//    showBackground = true,
//    widthDp = 320,
//    wallpaper = Wallpapers.GREEN_DOMINATED_EXAMPLE
//)
//@Composable
//fun GreetingPreview() {
//    MyApplicationTheme {
//        Greetings()
//    }
//}
//
//
////class MainActivity : ComponentActivity() {
////    override fun onCreate(savedInstanceState: Bundle?) {
////        super.onCreate(savedInstanceState)
////        enableEdgeToEdge()
////        setContent {
////            MyAppContent()
////        }
////    }
////
////    @Composable
////    fun MyAppContent() {
////        var user by remember { mutableStateOf<User?>(null) }
////        var showDetails by remember { mutableStateOf(false) }
////        var showData by remember { mutableStateOf(false) }
////        var weatherData by remember { mutableStateOf<VCWeather?>(null) }
////
////        Column(
////            modifier = Modifier
////                .fillMaxSize()
////                .padding(16.dp),
////            verticalArrangement = Arrangement.Center,
////            horizontalAlignment = Alignment.CenterHorizontally
////        ) {
////            Button(onClick = { showData = true }) {
////                Text(text = "View Weather Data")
////            }
////
////            // Display weather data when showData is true
////            if (showData) {
////                WeatherData(data = weatherData)
////            }
////        }
////
////        LaunchedEffect(showData) {
////            if (showData) {
////                try {
////                    // Fetch weather data from API
////                    val fetchedWeather = fetchData()
////                    weatherData = fetchedWeather
////                } catch (e: Exception) {
////                    // Handle error, e.g., show error message
////                    weatherData = null
////                }
////            }
////        }
////    }
////
////    @Composable
////    fun Greeting(name: String) {
////        Text(text = "Hello $name!")
////    }
////
////    @Composable
////    fun UserDetails(user: User) {
////        Column {
////            Text(text = "Name: ${user.name}")
////            Text(text = "Email: ${user.email}")
////            Text(text = "Age: ${user.age}")
////            Text(text = "Location: ${user.location}")
////
////            Button(onClick = { /* Hide details */ }) {
////                Text(text = "Hide Details")
////            }
////        }
////    }
////
////    @Composable
////    fun WeatherData(data: VCWeather?) {
////        data?.let {
////            Column(horizontalAlignment = Alignment.CenterHorizontally) {
////                Text(text = "Address: ${it.address}")
////                Text(text = "Temperature: ${it.temperature}")
////                Text(text = "Humidity: ${it.humidity}")
////            }
////        }
////    }
////
////    private suspend fun fetchUser(userId: String): User {
////        return RetrofitClient.getApiService().getUser(userId)
////    }
////
////    private suspend fun fetchData(): VCWeather {
////        return RetrofitClient.getVCAPI().getWeather()
////    }
////}
