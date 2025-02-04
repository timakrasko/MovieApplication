package ua.edu.sumdu.movielibrary.presentation.movie_details

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContentProviderCompat.requireContext
import coil.compose.AsyncImage
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import ua.edu.sumdu.movielibrary.data.Dto.MovieDto

@Composable
fun MovieDetailsScreen(
    movie: MovieDto,
    onNavigateBack: () -> Unit,
) {
    val viewModel: MovieDetailsViewModel = koinViewModel(parameters = { parametersOf(movie) })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = movie.title,
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
            AsyncImage(
                model = movie.imageUrl,
                contentDescription = "${movie.title} Poster",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(140.dp)
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(2.dp, Color.Gray, RoundedCornerShape(16.dp))
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp)
            ) {
                Text(
                    text = "Director: ${movie.director}",
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
                    movie.genres.forEach { genre ->
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
            text = movie.description,
            fontSize = 16.sp,
            lineHeight = 20.sp,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        Button(
            onClick = {
                viewModel.deleteMovie()
                onNavigateBack()
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = "Delete")
        }


        Button(
            onClick = {
                val userId = Firebase.auth.currentUser?.uid
                if (userId != null) {
                    viewModel.markMovieAsWatched(userId, movie)
                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = "Add to watched")
        }
    }
}