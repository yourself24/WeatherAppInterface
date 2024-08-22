package com.example.myapplication
import AdminDashboard
import LoginScreen
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.health.connect.datatypes.ExerciseRoute
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.myapplication.Dashboards.SensorDashboard
import com.example.myapplication.Dashboards.UserDashboard
import com.example.myapplication.Dashboards.YourBluetoothScreen
import com.example.myapplication.Models.User
import com.example.myapplication.authentication.HomeScreen
import com.example.myapplication.authentication.RegisterScreen
import com.example.myapplication.authentication.WelcomeScreen


import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.polidea.rxandroidble2.RxBleClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.util.UUID

// FusedLocationProviderClient - Main class for receiving location updates.
private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

// LocationRequest - Requirements for the location updates, i.e.,
// how often you should receive updates, the priority, etc.
private lateinit var locationRequest: LocationRequest

// LocationCallback - Called when FusedLocationProviderClient
// has a new Location
private lateinit var locationCallback: LocationCallback

// This will store current location info
private var currentLocation: ExerciseRoute.Location? = null


class MainActivity : ComponentActivity() {

    lateinit var rxBleClient: RxBleClient
    override fun onCreate(savedInstanceState: Bundle?) {

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        super.onCreate(savedInstanceState)
        rxBleClient = RxBleClient.create(this)
        setContent {
            MyAppNavigation()
        }
    }
}

@Composable
fun MyAppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "welcome_screen") {
        composable("welcome_screen"){ WelcomeScreen(navController) }
        composable(route = "login_screen"){LoginScreen(navController)}
        composable(route = "user_dashboard/{email}",
            arguments = listOf(navArgument("email"){type = NavType.StringType})){
                backStackEntry -> val email = backStackEntry.arguments?.getString("email")
            if (email != null) {
                UserDashboard(navController,email = email)
            }
        }
        composable("admin_dashboard"){AdminDashboard(navController)}

        composable("arduino_data"){ YourBluetoothScreen(navController) }
        composable("sensor_dashboard"){ SensorDashboard(navController) }
        composable(route = "home_screen/{email}",
            arguments = listOf(navArgument("email"){type = NavType.StringType})){
            backStackEntry -> val email = backStackEntry.arguments?.getString("email")
            HomeScreen(email = email)
        }

        composable("register_screen") { RegisterScreen(navController) }

    }
}
