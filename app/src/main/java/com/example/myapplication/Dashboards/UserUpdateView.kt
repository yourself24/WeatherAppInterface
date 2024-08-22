package com.example.myapplication.Dashboards

import android.widget.Toast
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.Models.UpdatedUser
import com.example.myapplication.Models.User
import com.example.myapplication.Retrofit.RetrofitClient
import kotlinx.coroutines.launch

@Composable
fun UserEditView(navController: NavController, email: String) {
    var name by remember { mutableStateOf("") }
    var emailState by remember { mutableStateOf(email) }
    var location by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var id by remember { mutableStateOf(0L) }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(email) {
        scope.launch {
            try {
                val user = RetrofitClient.getApiService().getUserByEmail(email)
                id = user.id
                name = user.name
                emailState = user.email
                location = user.location
                role = user.role
            } catch (e: Exception) {
                Toast.makeText(context, "Failed to load user data: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Edit User", style = MaterialTheme.typography.h5, modifier = Modifier.padding(bottom = 16.dp))

        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )

        TextField(
            value = email,
            onValueChange = { emailState = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )

        TextField(
            value = location,
            onValueChange = { location = it },
            label = { Text("Location") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )

        TextField(
            value = role,
            onValueChange = { role = it },
            label = { Text("Role") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )

        Button(
            onClick = {
                scope.launch {
                    try {
                        val updatedUser = User(
                             name,
                             email,
                            password,
                             location,
                             role
                        )
                        UpdatedUser()
                        // Call the API to update the user
                        val response = RetrofitClient.getApiService().updateUser(id, UpdatedUser(name,email,location,role))

                        // Handle the response

                            Toast.makeText(context, "User updated successfully!", Toast.LENGTH_LONG).show()


                    } catch (e: Exception) {
                        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Save Changes")
        }

        Button(
            onClick = {
                navController.navigateUp()
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Cancel")
        }
    }
}
