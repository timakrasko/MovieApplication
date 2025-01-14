package ua.edu.sumdu.movielibrary.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import ua.edu.sumdu.movielibrary.data.Dto.CreateScreenObject
import ua.edu.sumdu.movielibrary.data.Dto.LoginScreenObject
import ua.edu.sumdu.movielibrary.data.Dto.MainScreenDataObject
import ua.edu.sumdu.movielibrary.data.Dto.MovieDto
import ua.edu.sumdu.movielibrary.presentation.login.LoginScreen
import ua.edu.sumdu.movielibrary.presentation.main_screen.MainScreen
import ua.edu.sumdu.movielibrary.presentation.movie_create.MovieCreateScreen
import ua.edu.sumdu.movielibrary.presentation.movie_details.MovieDetailsScreen

@Composable
fun MovieNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
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
        composable<MainScreenDataObject> {
            MainScreen(
                navigateToMovieDetails =  { movie ->
                    navController.navigate(movie)
                },
                navigateToMovieCreate = {
                    navController.navigate(CreateScreenObject)
                }
            )
        }


        composable<MovieDto> { backStackEntry ->
            val movie = backStackEntry.toRoute<MovieDto>()
            MovieDetailsScreen(movie)
        }

        composable<CreateScreenObject> {
            MovieCreateScreen()
        }
    }
}