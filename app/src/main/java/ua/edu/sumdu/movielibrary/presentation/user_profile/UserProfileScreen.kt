package ua.edu.sumdu.movielibrary.presentation.user_profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.ui.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import ua.edu.sumdu.movielibrary.data.dto.MovieDto
import ua.edu.sumdu.movielibrary.data.dto.UserDto
import ua.edu.sumdu.movielibrary.data.dto.toMovieDto
import ua.edu.sumdu.movielibrary.domain.Movie
import ua.edu.sumdu.movielibrary.domain.User

@Composable
fun UserProfileScreen(
    userDto: UserDto,
    navigateToMovieDetails: (MovieDto) -> Unit,
) {
    val viewModel: UserProfileViewModel = koinViewModel(parameters = { parametersOf(userDto.uid) })
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val user = state.user

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // User Profile Picture


        Spacer(modifier = Modifier.height(8.dp))

        // User Email
        Text(
            text = user?.email ?: "No Email",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Button(
            onClick = {
                viewModel.addFriend(user?.uid ?: "")
            },
        ) {
            Text(text = "Add Friend")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Watched Movies Section
        Text(
            text = "Watched Movies",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(state.watchedMovies) { movie ->
                MovieItem(movie, navigateToMovieDetails)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Planed Movies",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(state.planedMovies) { movie ->
                MovieItem(movie, navigateToMovieDetails)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Friends",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(8.dp))

        when {
            state.friends.isEmpty() -> {
                Text(
                    text = "No friends yet",
                    modifier = Modifier.padding(16.dp)
                )
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.weight(1f))
                    {
                        items(state.friends) { friend ->
                            // FriendItem - потрібно створити окремий Composable
                            FriendItem(friend = friend)
                        }
                    }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        //logout
    }
}

@Composable
fun MovieItem(
    movie: Movie,
    navigateToMovieDetails: (MovieDto) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
            .clickable { navigateToMovieDetails(movie.toMovieDto()) }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = movie.imageUrl,
            contentDescription = "Movie Poster",
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(text = movie.title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(text = movie.director, fontSize = 14.sp, color = Color.Gray)
        }

        // Додаємо відображення рейтингу справа
        movie.rating?.let { rating ->
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(Color.DarkGray, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = rating.toString(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
fun FriendItem(
    friend: User,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = friend?.email ?: "Loading...",
                fontSize = 16.sp)
        }
    }
}