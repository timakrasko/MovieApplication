package ua.edu.sumdu.movielibrary.presentation.user_profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import ua.edu.sumdu.movielibrary.data.dto.MovieDto
import ua.edu.sumdu.movielibrary.data.dto.UserDto
import ua.edu.sumdu.movielibrary.data.dto.toMovieDto
import ua.edu.sumdu.movielibrary.data.dto.toUserDto
import ua.edu.sumdu.movielibrary.domain.Movie
import ua.edu.sumdu.movielibrary.domain.User

@Composable
fun UserProfileScreen(
    userDto: UserDto,
    navigateToMovieDetails: (MovieDto) -> Unit,
    navigateToFriendDetails: (UserDto) -> Unit
) {
    val viewModel: UserProfileViewModel = koinViewModel(parameters = {
        parametersOf(userDto.uid)
    })
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val user = state.user

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = user?.username ?: "No Username",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                if (state.showFriendActionButton) {
                    Button(
                        onClick = { viewModel.toggleFriendship(user?.uid.toString()) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (state.isFriend) Color.Red else Color.Blue
                        )
                    ) {
                        Text(
                            text = if (state.isFriend) "Remove Friend" else "Add Friend",
                            color = Color.White
                        )
                    }
                }
            }
        }

        item {
            MovieSection(
                title = "Watched Movies",
                movies = state.watchedMovies,
                navigateToMovieDetails = navigateToMovieDetails
            )
        }

        item {
            MovieSection(
                title = "Planned Movies",
                movies = state.planedMovies,
                navigateToMovieDetails = navigateToMovieDetails
            )
        }

        item {
            FriendsSection(
                friends = state.friends,
                navigateToFriendDetails
            )
        }
    }
}

@Composable
fun MovieSection(
    title: String,
    movies: List<Movie>,
    navigateToMovieDetails: (MovieDto) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        if (movies.isEmpty()) {
            Text(
                text = "No movies yet",
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        } else {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(movies) { movie ->
                    MovieCard(
                        movie = movie,
                        navigateToMovieDetails = navigateToMovieDetails,
                        modifier = Modifier.width(150.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun MovieCard(
    movie: Movie,
    navigateToMovieDetails: (MovieDto) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(150.dp)
            .clickable { navigateToMovieDetails(movie.toMovieDto()) },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            AsyncImage(
                model = movie.imageUrl,
                contentDescription = "Movie Poster",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(2/3f)
                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    text = movie.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                movie.rating?.let { rating ->
                    Text(
                        text = "★ $rating",
                        fontSize = 12.sp,
                        color = Color(0xFFFFA500)
                    )
                }
            }
        }
    }
}

@Composable
fun FriendsSection(
    friends: List<User>,
    navigateToFriendDetails: (UserDto) -> Unit
    ) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = "Friends",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        if (friends.isEmpty()) {
            Text(
                text = "No friends yet",
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 200.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(friends) { friend ->
                    FriendCard(friend = friend, navigateToFriendDetails)
                }
            }
        }
    }
}

@Composable
fun FriendCard(
    friend: User,
    navigateToFriendDetails: (UserDto) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable{navigateToFriendDetails(friend.toUserDto())},
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = friend.username,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f)
            )
        }
    }
}