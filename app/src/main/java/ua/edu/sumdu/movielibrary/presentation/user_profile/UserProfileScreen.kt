package ua.edu.sumdu.movielibrary.presentation.user_profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
        Text(
            text = user?.username ?: "No Username",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        if (state.showFriendActionButton) {
                Button(onClick = { viewModel.toggleFriendship(user?.uid.toString()) }) {
                    Text(
                        if (state.isFriend) "Remove Friend"
                        else "Add Friend"
                    )
                }
        }

        Spacer(modifier = Modifier.height(16.dp))

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
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clickable { navigateToMovieDetails(movie.toMovieDto()) },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = movie.imageUrl,
                contentDescription = "Movie Poster",
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(4.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = movie.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1
                )
                Text(
                    text = movie.director,
                    fontSize = 12.sp,
                    maxLines = 1,
                    color = Color.Gray
                )
            }

            movie.rating?.let { rating ->
                Text(
                    text = rating.toString(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}

@Composable
fun FriendItem(
    friend: User,
) {
    Text(
        text = friend.username,
        fontSize = 14.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 4.dp)
    )
}