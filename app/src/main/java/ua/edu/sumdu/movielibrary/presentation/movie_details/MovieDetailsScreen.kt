package ua.edu.sumdu.movielibrary.presentation.movie_details

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import ua.edu.sumdu.movielibrary.data.dto.UpdateScreenObject

@Composable
fun MovieDetailsScreen(
    movieId: String,
    onNavigateBack: () -> Unit,
    onNavigateToEditScreen: (UpdateScreenObject) -> Unit,
) {
    val viewModel: MovieDetailsViewModel = koinViewModel(parameters = { parametersOf(movieId) })
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val movie = state.movie

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = movie?.title ?: "No title",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.Top
        ) {
            if (movie?.imageUrl.isNullOrEmpty()) {
                Box(
                    modifier = Modifier
                        .width(140.dp)
                        .height(200.dp)
                        .background(Color.Gray),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No Image",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                AsyncImage(
                    model = movie.imageUrl,
                    contentDescription = "Poster",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(140.dp)
                        .height(200.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .border(2.dp, Color.Gray, RoundedCornerShape(16.dp))
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp)
            ) {
                Text(
                    text = "Director: ${movie?.director}",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Genres:",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Column(
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    movie?.genres?.forEach { genre ->
                        Text(
                            text = genre,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Description",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Text(
            text = movie?.description ?: "No description",
            fontSize = 16.sp,
            lineHeight = 20.sp,
            modifier = Modifier.padding(horizontal = 8.dp)
        )


        MovieRatingBar(
            isWatched = state.isWatched,
            currentRating = state.movieRating,
            onRatingChanged = { rating ->
                viewModel.rateMovie(rating)
            }
        )


        if (state.isAdmin)
        Button(
            onClick = {
                viewModel.deleteMovie()
                onNavigateBack()
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = "Delete")
        }

        if (state.isAdmin)
        Button(
            onClick = {
                onNavigateToEditScreen(UpdateScreenObject(movieId))
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = "Edit")
        }

        if (!state.isWatched) {
            Button(
                onClick = {
                    viewModel.markMovieAsWatched()
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(text = "Add to watched")
            }

            if (!state.isPlanned) {
                Button(
                    onClick = {
                        viewModel.markMovieAsPlaned()
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(text = "Add to planed")
                }
            } else {
                Button(
                    onClick = {
                        viewModel.removePlanedMovie()
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(text = "Remove from planed")
                }
            }
        } else {
            Button(
                onClick = {
                    viewModel.removeWatchedMovie()
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(text = "Remove from watched")
            }
        }
    }
}

@Composable
fun MovieRatingBar(
    isWatched: Boolean,
    currentRating: Int?,
    onRatingChanged: (Int) -> Unit
) {
    if (isWatched) {
        Row {
            (1..5).forEach { star ->
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Star $star",
                    tint = if (currentRating != null && star <= currentRating) Color.Yellow else Color.Gray,
                    modifier = Modifier
                        .clickable { onRatingChanged(star) }
                        .padding(4.dp)
                )
            }
        }
    }
}