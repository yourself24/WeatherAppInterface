import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.sharp.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.example.myapplication.Models.UpdatedUser
import com.example.myapplication.Retrofit.RetrofitClient
import kotlinx.coroutines.launch
import com.example.myapplication.Models.User
import com.example.myapplication.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboard(navController: NavController) {
    var selectedUser by remember { mutableStateOf<User?>(null) }
    var userList by remember { mutableStateOf<List<User>>(emptyList()) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var showCreateUserDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        scope.launch {
            try {
                userList = RetrofitClient.getApiService().getAllUsers()
            } catch (e: Exception) {
                Toast.makeText(context, "Failed to load users because of: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.admin), // Replace with your image resource ID
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize()
        )

        // Content Overlay
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Users List
            UserList(
                users = userList,
                onUserSelected = { user ->
                    selectedUser = user
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Row with Create User and Logout Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = { showCreateUserDialog = true }) {
                    Text("Create User")
                }
                Button(onClick = {
                    scope.launch {
                        try{
                        RetrofitClient.getApiService().logOut()
                        }catch (e:Exception){
                            navController.navigate("welcome_screen")
                            Toast.makeText(context,"Failed to load users because of: ${e.message}",
                                Toast.LENGTH_LONG).show()

                        }

                    }
                    navController.navigate("login_screen")
                }) {
                    Text("Logout")
                }
            }

            // User Edit Dialog
            selectedUser?.let { user ->
                UserEditDialog(
                    user = user,
                    onDismiss = { selectedUser = null },
                    onSave = { selectedUser = null }
                )
            }

            // Show Create User Dialog
            if (showCreateUserDialog) {
                CreateUserDialog(
                    onDismiss = { showCreateUserDialog = false },
                    onCreate = {
                        scope.launch {
                            try {
                                userList = RetrofitClient.getApiService().getAllUsers()
                                Toast.makeText(context, "User created successfully", Toast.LENGTH_LONG).show()
                            } catch (e: Exception) {
                                Toast.makeText(context, "Failed to create user: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                            showCreateUserDialog = false
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun UserList(users: List<User>, onUserSelected: (User) -> Unit) {
    LazyColumn {
        items(users) { user ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable { onUserSelected(user) },
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(user.name, fontWeight = FontWeight.Bold)
                    Text(user.email, fontSize = 12.sp, color = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun CreateUserDialog(onDismiss: () -> Unit, onCreate: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("") }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Dialog(onDismissRequest = onDismiss, properties = DialogProperties()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Create New User", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Spacer(modifier = Modifier.height(8.dp))

                TextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
                Spacer(modifier = Modifier.height(8.dp))
                TextField(value = password, onValueChange = { password = it }, label = { Text("Password") })
                Spacer(modifier = Modifier.height(8.dp))
                TextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
                Spacer(modifier = Modifier.height(8.dp))
                TextField(value = role, onValueChange = { role = it }, label = { Text("Role") })
                Spacer(modifier = Modifier.height(8.dp))
                TextField(value = location, onValueChange = { location = it }, label = { Text("Location") })
                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        coroutineScope.launch {
                            try {
                                val randomString = (1..12)
                                    .map { ('A'..'Z') + ('a'..'z') + ('0'..'9') }
                                    .map { it.random() }
                                    .joinToString("")
                                val newUser = User(name, email, password, location, role)
                                RetrofitClient.getApiService().createUser(newUser)

                                onCreate()
                            } catch (e: Exception) {
                                Toast.makeText(context, "Failed to create user: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                        }
                    }) {
                        Text("Create")
                    }
                }
            }
        }
    }
}

@Composable
fun UserEditDialog(user: User, onDismiss: () -> Unit, onSave: (User) -> Unit) {
    var name by remember { mutableStateOf(user.name) }
    var email by remember { mutableStateOf(user.email) }
    var location by remember { mutableStateOf(user.location) }
    var role by remember { mutableStateOf(user.role) }
    var id by remember { mutableStateOf(user.id) }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Dialog(onDismissRequest = onDismiss, properties = DialogProperties()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                TextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
                Spacer(modifier = Modifier.height(8.dp))
                TextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
                Spacer(modifier = Modifier.height(8.dp))
                TextField(value = id.toString(), onValueChange = { id = it.toLongOrNull() }, label = { Text("UID") })
                Spacer(modifier = Modifier.height(8.dp))
                TextField(value = role, onValueChange = { role = it }, label = { Text("Role") })
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Location") }
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Save and Cancel Buttons Row
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        coroutineScope.launch {
                            try {
                                val updatedUser = UpdatedUser(
                                    name,
                                    email,
                                    location,
                                    role
                                )
                                val response = RetrofitClient.getApiService().updateUser(id, updatedUser)
                                // Handle response or navigate away
                            } catch (e: Exception) {
                                Toast.makeText(context, "Failed to update user! Error: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                        }
                    }) {
                        Text("Save Changes")
                    }
                }

                // Remove User Button
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                    Button(onClick = {
                        coroutineScope.launch {
                            try {
                                RetrofitClient.getApiService().deleteUser(id)
                                // Handle response or navigate away
                            } catch (e: Exception) {
                                Toast.makeText(context, "Failed to remove user! Error: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                        }
                    }) {
                        Text("Remove User")
                    }
                }
            }
        }
    }
}