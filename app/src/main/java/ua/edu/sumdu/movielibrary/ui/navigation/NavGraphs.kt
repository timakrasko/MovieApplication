package ua.edu.sumdu.movielibrary.ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

@Serializable
object MovieGraph

@Serializable
data class Movie(val id: Int, val title: String)

@Serializable
object MovieList

@Composable
fun MovieNavHostt(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = MovieGraph,
        modifier = modifier
    ) {
        movieGraph(navController)
    }
}

fun NavGraphBuilder.movieGraph(navController: NavController) {
    navigation<MovieGraph>(startDestination = MovieList) {
        composable<MovieList> {
            MovieListScreen{navData ->
                navController.navigate(navData)
            }
        }

        composable<Movie> { backStackEntry ->
            val movie = backStackEntry.toRoute<Movie>()
            MovieScreen(movie)
        }
    }
}

@Composable
fun MovieListScreen(
    onNavigationToMovie: (Movie) -> Unit
) {
    val movies = listOf(
        Movie(1, "Inception"),
        Movie(2, "Interstellar"),
        Movie(3, "The Dark Knight")
    )

    Column {
        movies.forEach { movie ->
            Button(onClick = {
                onNavigationToMovie(movie)
            }) {
                Text(movie.title)
            }
        }
    }
}

@Composable
fun MovieScreen(movie: Movie) {
    Text(text = "Деталі фільму з ID: ${movie.id} is ${movie.title}")
}

