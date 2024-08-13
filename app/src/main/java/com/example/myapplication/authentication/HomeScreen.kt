package com.example.myapplication.authentication

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.myapplication.Models.User
import com.example.myapplication.Retrofit.RetrofitClient

@Composable
fun HomeScreen(email: String?) {
    val context = LocalContext.current
    var user by remember { mutableStateOf<User?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(email) {
        email?.let{
            try{
                println(email)
                user = RetrofitClient.getApiService().getUserByEmail(it)
            }catch(e:Exception){
                Toast.makeText(context,"Failed to fetch user :${e.message}",Toast.LENGTH_LONG).show()
                errorMessage = e.message ?: "Unknown error"

            }
            finally{
                isLoading = false
            }
        }

    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if(isLoading){
            CircularProgressIndicator()
        }
        else{
            user?.let{
                Text("Welcome,${it.name}")
            }
            errorMessage?.let {
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Error: $it", color = MaterialTheme.colorScheme.error)
            }

        }
    }
}
