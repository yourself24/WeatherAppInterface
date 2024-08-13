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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboard(navController: NavController) {
    var selectedUser by remember { mutableStateOf<User?>(null) }
    var drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

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
                                Icon(imageVector = Icons.AutoMirrored.Sharp.ExitToApp, contentDescription = "Menu")
                            }
                        },
                        actions = {
                            IconButton(onClick = {
                                // Handle logout logic
                                navController.navigate("login_screen")
                            }) {
                                Icon(imageVector = Icons.AutoMirrored.Sharp.ExitToApp, contentDescription = "Logout")
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
                            users = getUsers(), // Replace with actual user data source
                            onUserSelected = { user ->
                                selectedUser = user
                            }
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        // User Details
                        selectedUser?.let {
                            UserDetails(user = it, onSave = { updatedUser ->
                                // Update user logic here
                                selectedUser = null
                            })
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
fun UserDetails(user: User, onSave: (User) -> Unit) {
    var name by remember { mutableStateOf(user.name) }
    var email by remember { mutableStateOf(user.email) }
    var location by remember { mutableStateOf(user.location) }

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
            TextField(value = location, onValueChange = { location = it }, label = { Text("Location") })

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                onSave(user.copy(name = name, email = email, location = location))
            }) {
                Text("Save Changes")
            }
        }
    }
}

data class User(val id: String, val name: String, val email: String, val location: String)

fun getUsers(): List<User> {
    return listOf(
        User(id = "1", name = "John Doe", email = "john.doe@example.com", location = "New York"),
        User(id = "2", name = "Jane Smith", email = "jane.smith@example.com", location = "Los Angeles"),
        // Add more users here
    )
}
