package ua.edu.sumdu.movielibrary.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import ua.edu.sumdu.movielibrary.data.LoginScreenObject
import ua.edu.sumdu.movielibrary.data.MainScreenDataObject
import ua.edu.sumdu.movielibrary.ui.login.LoginScreen
import ua.edu.sumdu.movielibrary.ui.main_screen.MainScreen

@Composable
fun MovieNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = LoginScreenObject,
        modifier = modifier
    ) {
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