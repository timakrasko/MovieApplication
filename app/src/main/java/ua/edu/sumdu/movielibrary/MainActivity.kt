package ua.edu.sumdu.movielibrary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import ua.edu.sumdu.movielibrary.data.LoginScreenObject
import ua.edu.sumdu.movielibrary.data.MainScreenDataObject
import ua.edu.sumdu.movielibrary.ui.login.LoginScreen
import ua.edu.sumdu.movielibrary.ui.main_screen.MainScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = LoginScreenObject
            ){
                composable<LoginScreenObject> {
                    LoginScreen { navData ->
                        navController.navigate(navData)
                    }
                }

                composable<MainScreenDataObject> { navEntry ->
                    val navData = navEntry.toRoute<MainScreenDataObject>()
                    MainScreen()
                }
            }
        }
    }
}