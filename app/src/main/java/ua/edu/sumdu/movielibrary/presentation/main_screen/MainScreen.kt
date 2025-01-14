package ua.edu.sumdu.movielibrary.presentation.main_screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import ua.edu.sumdu.movielibrary.data.Dto.MovieDto
import ua.edu.sumdu.movielibrary.data.Dto.toMovieDto
import ua.edu.sumdu.movielibrary.domain.Movie
import ua.edu.sumdu.movielibrary.ui.theme.PurpleGrey40
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import ua.edu.sumdu.movielibrary.data.Dto.CreateScreenObject
import ua.edu.sumdu.movielibrary.presentation.main_screen.bottom_menu.BottomMenu

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(
    viewModel: MainViewModel = koinViewModel(),
    navigateToMovieDetails: (MovieDto) -> Unit,
    navigateToMovieCreate: (CreateScreenObject) -> Unit
) {
    val movieListState by viewModel.movieListState.collectAsStateWithLifecycle()

    if (movieListState.isLoading) {
         Log.d("123", "123")
    } else {
        ModalNavigationDrawer(
            modifier = Modifier.fillMaxWidth(),
            drawerContent = {
                Column(
                    modifier = Modifier.fillMaxWidth(0.7f)
                ) {
                    Text("Drawer Content")
                }
            }
        ) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                bottomBar = { BottomMenu() }
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Button(
                        onClick = { navigateToMovieCreate(CreateScreenObject) },
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(text = "Add Movie")
                    }
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        items(movieListState.movies) { movie ->
                            MovieCard(movie, navigateToMovieDetails)
                        }
                    }

                    if (movieListState.isLoading) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    }

                    movieListState.errorMessage?.let {
                        Text(text = it, color = Color.Red)
                    }
                }
            }
        }
    }
}


@Composable
fun MovieCard(
    movie: Movie,
    onMovieClick: (MovieDto) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        onClick = {
            onMovieClick(movie.toMovieDto())
        },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            AsyncImage(
                model = movie.imageUrl,
                contentDescription = "${movie.title} Poster",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(120.dp)
                    .height(180.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.width(4.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp)
            ) {
                Text(
                    text = movie.title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 20.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Director: ${movie.director}",
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(8.dp))

                movie.genres.forEach{
                    Text(
                        text = it,
                        maxLines = 4,
                    )
                }
            }
        }
    }
}

@Composable
fun Header() {
    Column(
        Modifier
            .fillMaxWidth()
            .height(170.dp)
            .background(PurpleGrey40),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(130.dp))
    }
}

@Composable
fun Body() {
    val genresList = listOf(
        "Horror",
        "Fantasy",
        "Drama"
    )
    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Genres",
                fontSize = 20.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.Gray)
            )
            LazyColumn(Modifier.fillMaxSize()) {
                items(genresList) { item ->
                    Column(Modifier.fillMaxWidth().clickable {

                    }) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = item,
                            color = Color.Black,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentWidth()
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(Color.Gray)
                        )
                    }
                }
            }
        }
    }
}