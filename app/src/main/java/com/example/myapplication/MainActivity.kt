package com.example.myapplication
import AdminDashboard
import LoginScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.myapplication.authentication.HomeScreen
import com.example.myapplication.authentication.RegisterScreen
import com.example.myapplication.authentication.WelcomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        composable("login_screen") { LoginScreen(navController) }
        composable("admin_dashboard"){AdminDashboard(navController)}
        composable(route = "home_screen/{email}",
            arguments = listOf(navArgument("email"){type = NavType.StringType})){
            backStackEntry -> val email = backStackEntry.arguments?.getString("email")
            HomeScreen(email = email)
        }

        composable("register_screen") { RegisterScreen(navController) }

    }
}
