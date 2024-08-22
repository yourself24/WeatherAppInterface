package com.example.myapplication.authentication

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


fun checkInternetConnection(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = connectivityManager.activeNetwork ?: return false
    val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
    return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}
@SuppressLint("InlinedApi")
@Composable
fun WelcomeScreen(navController: NavController){
 val context = LocalContext.current
 val isConnected = remember { mutableStateOf(checkInternetConnection(context)) }
    val coroutineScope = rememberCoroutineScope()

    val permissionsRequestor = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) {
        isGranted: Boolean ->
        if(isGranted){
            navController.navigate("login_screen")

        }
        else{
            navController.navigate("register_screen")
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome to the App!",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        if (isConnected.value) {
            Button(
                onClick = {
                    // Ask for location permission for online mode
                    permissionsRequestor.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)

                },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            ) {
                Text("Go Online")
            }
        }

        Button(
            onClick = {
                navController.navigate("arduino_data") // Navigate to offline mode
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Go Offline")
        }

    }

}