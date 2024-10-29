package ua.edu.sumdu.movielibrary.ui.navigation

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import ua.edu.sumdu.movielibrary.R
import ua.edu.sumdu.movielibrary.data.Movie
import java.io.ByteArrayOutputStream

@Serializable
object MovieGraph

@Serializable
data class MovieDto(
    val title: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val director: String = "",
    val genres: List<String> = listOf()
)

fun Movie.toMovieDto(): MovieDto{
    return MovieDto(
        title = title,
        description = description,
        imageUrl = imageUrl,
        director = director,
        genres = genres
    )
}

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
    val fs = Firebase.firestore
    val storage = Firebase.storage.reference.child("images")

    Button(onClick = {
        val task = storage.child("lotr.jpeg").putBytes(
            bitmapToByteArray(context)
        )
        task.addOnSuccessListener { uploadTask ->
            uploadTask.metadata?.reference
                ?.downloadUrl?.addOnCompleteListener{ uriTask ->
                    saveMovie(fs, uriTask.result.toString())
            }
        }
    }) { Text(text = "Add") }

    val list = remember {
        mutableStateOf(emptyList<Movie>())
    }

    val listener = fs.collection("movies").addSnapshotListener { snapShot, exception ->
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

private fun bitmapToByteArray(context: Context): ByteArray{
    val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.lotr)
    val boas = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, boas)
    return boas.toByteArray()
}

private fun saveMovie(fs: FirebaseFirestore, url: String){
    fs.collection("movies")
        .document()
        .set(
            Movie(
                "title1",
                "desc1",
                url,
                "dir1",
            )
        )
}

