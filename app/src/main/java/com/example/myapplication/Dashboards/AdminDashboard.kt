import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.sharp.ExitToApp
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.myapplication.Models.UpdatedUser
import com.example.myapplication.Retrofit.RetrofitClient
import kotlinx.coroutines.launch
import com.example.myapplication.Models.User


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboard(navController: NavController) {
    var selectedUser by remember { mutableStateOf<User?>(null) }
    var drawerState = rememberDrawerState(DrawerValue.Closed)
    var userList by remember{ mutableStateOf<List<User>>(emptyList()) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        scope.launch {
            try {
                userList = RetrofitClient.getApiService().getAllUsers()
            }
            catch(e:Exception){
                Toast.makeText(context,"Failed to load users because of: ${e.message}",Toast.LENGTH_LONG).show()
            }
        }
    }


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(navController = navController)
        },
        content = {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Admin Dashboard") },
                        navigationIcon = {
                            IconButton(onClick = {
                                scope.launch { drawerState.open() }
                            }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Sharp.ExitToApp,
                                    contentDescription = "Menu"
                                )
                            }
                        },
                        actions = {
                            IconButton(onClick = {
                                // Handle logout logic
                                navController.navigate("login_screen")
                            }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Sharp.ExitToApp,
                                    contentDescription = "Logout"
                                )
                            }
                        }
                    )
                },
                content = { paddingValues ->
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Users List

                        UserList(
                            users = userList, // Replace with actual user data source
                            onUserSelected = { user ->
                                selectedUser = user
                            }
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        // User Details
                        selectedUser?.let {user->UserEditDialog(user = user,
                            onDismiss = {selectedUser= null},

                                onSave= { selectedUser= null

                                }


                            )

                        }
                    }
                }
            )
            if (drawerState.isOpen) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f))
                        .zIndex(1f) // Ensures this overlay is on top of the content
                        .clickable {
                            // Close the drawer when the overlay is clicked
                            scope.launch { drawerState.close() }
                        }
                )
            }

        }
    )
}

@Composable
fun DrawerContent(navController: NavController) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("User Management", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        // Add more options here, like "Settings", "Reports", etc.
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
                Spacer(modifier = Modifier.height(8.dp))
                TextField(value = role, onValueChange = { role = it }, label = { Text("Role") })
                TextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Location") })
                Spacer(modifier = Modifier.height(8.dp))



                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        coroutineScope.launch {
                            try{
                                val updatedUser = UpdatedUser(
                                     name,
                                    email,
                                    location,
                                    role


                                )
                                val response = RetrofitClient.getApiService().updateUser(id,updatedUser)

                            }
                            catch(e:Exception){
                                Toast.makeText(context,"Failed to update user!Error: ${e.message}",Toast.LENGTH_LONG).show()

                            }
                        }

                    }
                    ) {
                        Text("Save Changes")
                    }
                }


            }
        }
    }
}



