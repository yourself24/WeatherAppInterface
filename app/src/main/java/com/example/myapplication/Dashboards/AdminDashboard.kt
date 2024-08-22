import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.sharp.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboard(navController: NavController) {
    var selectedUser by remember { mutableStateOf<User?>(null) }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    var userList by remember { mutableStateOf<List<User>>(emptyList()) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var showCreateUserDialog by remember { mutableStateOf(false) }
    var showDeleteUserDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        scope.launch {
            try {
                userList = RetrofitClient.getApiService().getAllUsers()
            } catch (e: Exception) {
                Toast.makeText(context, "Failed to load users because of: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                navController = navController,
                onCreateUser = { showCreateUserDialog = true },
                onDeleteUser = { showDeleteUserDialog = true }
            )
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
                                scope.launch {
                                    RetrofitClient.getApiService().logOut()
                                }
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
                        selectedUser?.let { user ->
                            UserEditDialog(user = user,
                                onDismiss = { selectedUser = null },
                                onSave = { selectedUser = null }
                            )
                        }
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

                    // Show Delete User Dialog
                    if (showDeleteUserDialog) {
                        DeleteUserDialog(
                            userList = userList,
                            onDismiss = { showDeleteUserDialog = false },
                            onDelete = {
                                scope.launch {
                                    try {
                                        userList = RetrofitClient.getApiService().getAllUsers()
                                        Toast.makeText(context, "User deleted successfully", Toast.LENGTH_LONG).show()
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "Failed to delete user: ${e.message}", Toast.LENGTH_LONG).show()
                                    }
                                    showDeleteUserDialog = false
                                }
                            }
                        )
                    }
                }
            )
        }
    )
}

@Composable
fun CreateUserDialog(onDismiss: () -> Unit, onCreate: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
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
                                val newUser = User(name,email,randomString,location,role)
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
fun DeleteUserDialog(userList: List<User>, onDismiss: () -> Unit, onDelete: () -> Unit) {
    // Implement the Delete User Dialog UI and logic
    // Display a list of users with options to delete a selected user
}

@Composable
fun DrawerContent(navController: NavController, onCreateUser: () -> Unit, onDeleteUser: () -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("User Management", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = onCreateUser,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.textButtonColors(
                contentColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text("Create User")
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            onClick = onDeleteUser,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.textButtonColors(
                contentColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text("Delete User")
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
                    Button(onClick = {
                        coroutineScope.launch {
                            RetrofitClient.getApiService().deleteUser(id)
                        }
                    }) {
                        Text("Remove User")
                    }
                }


            }
        }
    }
}



