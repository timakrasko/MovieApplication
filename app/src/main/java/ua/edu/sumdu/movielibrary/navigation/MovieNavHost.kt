package ua.edu.sumdu.movielibrary.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.toRoute
import ua.edu.sumdu.movielibrary.R
import ua.edu.sumdu.movielibrary.data.Dto.CreateScreenObject
import ua.edu.sumdu.movielibrary.data.Dto.LoginScreenObject
import ua.edu.sumdu.movielibrary.data.Dto.MainScreenDataObject
import ua.edu.sumdu.movielibrary.data.Dto.MovieDto
import ua.edu.sumdu.movielibrary.data.Dto.UserProfileScreenDataObject
import ua.edu.sumdu.movielibrary.presentation.login.LoginScreen
import ua.edu.sumdu.movielibrary.presentation.main_screen.MainScreen
import ua.edu.sumdu.movielibrary.presentation.movie_create.MovieCreateScreen
import ua.edu.sumdu.movielibrary.presentation.movie_details.MovieDetailsScreen
import ua.edu.sumdu.movielibrary.presentation.user_profile.UserProfileScreen

@Composable
fun MovieNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val topLevelRoutes = listOf(
        TopLevelRoute("Profile", MainScreenDataObject),
        TopLevelRoute("Friends", UserProfileScreenDataObject)
    )
    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            val hideBottomBarScreens = setOf(LoginScreenObject)
            val shouldShowBottomBar =
                currentDestination?.route !in hideBottomBarScreens.map { it.javaClass.canonicalName }

            if (shouldShowBottomBar) {
                NavigationBar {
                    topLevelRoutes.forEach { topLevelRoute ->
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    imageVector = if (currentDestination?.route == topLevelRoute.route.javaClass.canonicalName) ImageVector.vectorResource(
                                        R.drawable.ic_favs
                                    ) else ImageVector.vectorResource(R.drawable.ic_settings),
                                    contentDescription = topLevelRoute.name
                                )
                            },
                            label = { Text(topLevelRoute.name) },
                            selected = currentDestination?.route == topLevelRoute.route.toString(),
                            onClick = {
                                navController.navigate(topLevelRoute.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }

                                    launchSingleTop = true

                                    restoreState = true
                                }

                            }
                        )
                    }
                }
            }

        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = LoginScreenObject,
            modifier = modifier.padding(innerPadding)
        ) {
            composable<LoginScreenObject> {
                LoginScreen(
                    onNavigateToUserScreen = {
                        navController.navigate(UserProfileScreenDataObject)
                    },
                    onNavigationToMainScreen = { navData ->
                        navController.navigate(navData)
                    }
                )
            }

            composable<MainScreenDataObject> {
                MainScreen(
                    navigateToMovieDetails = { movie ->
                        navController.navigate(movie)
                    },
                    navigateToMovieCreate = {
                        navController.navigate(CreateScreenObject)
                    }
                )
            }


            composable<MovieDto> { backStackEntry ->
                val movie = backStackEntry.toRoute<MovieDto>()
                MovieDetailsScreen(
                    movie
                ) { navController.popBackStack() }
            }

            composable<CreateScreenObject> {
                MovieCreateScreen { navController.popBackStack() }
            }

            composable<UserProfileScreenDataObject> {
                UserProfileScreen()
            }
        }
    }
}

data class TopLevelRoute<T : Any>(val name: String, val route: T)