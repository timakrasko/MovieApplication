package ua.edu.sumdu.movielibrary.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import coil.compose.AsyncImage
import kotlinx.serialization.Serializable
import ua.edu.sumdu.movielibrary.data.Dto.MovieDto
import ua.edu.sumdu.movielibrary.data.Dto.MovieListObject
import ua.edu.sumdu.movielibrary.data.Dto.toMovieDto
import ua.edu.sumdu.movielibrary.domain.Movie
import ua.edu.sumdu.movielibrary.data.FireBaseRepository

@Serializable
object MovieGraph

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
    navigation<MovieGraph>(startDestination = MovieListObject) {
        composable<MovieListObject> {
            MovieListScreen { navData ->
                navController.navigate(navData)
            }
        }

        composable<MovieDto> { backStackEntry ->
            val movie = backStackEntry.toRoute<MovieDto>()
            MovieScreen(movie)
        }
    }
}

@Composable
fun MovieListScreen(
    onNavigationToMovie: (MovieDto) -> Unit
) {
    val context = LocalContext.current
    val fireBaseRepository = FireBaseRepository()

    Button(onClick = {
        val task = fireBaseRepository.getImageTask(context)
        task.addOnSuccessListener { uploadTask ->
            uploadTask.metadata?.reference
                ?.downloadUrl?.addOnCompleteListener{ uriTask ->
                    fireBaseRepository.saveMovie(uriTask.result.toString())
            }
        }
    }) { Text(text = "Add") }

    val list = remember {
        mutableStateOf(emptyList<Movie>())
    }

    val listener = fireBaseRepository.getMovies().addSnapshotListener { snapShot, exception ->
        list.value = snapShot?.toObjects(Movie::class.java) ?: emptyList()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f)
        ) {
            items(list.value) { movie ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    onClick = {
                        onNavigationToMovie(movie.toMovieDto())
                    }
                ) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        AsyncImage(model = movie.imageUrl , contentDescription = null,
                            modifier = Modifier.height(100.dp).width(100.dp))
                        Text(text = movie.title)
                    }
                }
            }
        }
    }
}

@Composable
fun MovieScreen(movie: MovieDto) {
    Text(text = "Movie ${movie.title}, ${movie.director}")
}


