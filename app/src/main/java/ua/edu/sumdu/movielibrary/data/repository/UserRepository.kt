package ua.edu.sumdu.movielibrary.data.repository

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import ua.edu.sumdu.movielibrary.data.Dto.MovieDto
import ua.edu.sumdu.movielibrary.domain.Movie
import ua.edu.sumdu.movielibrary.domain.User

interface UserRepository {
    suspend fun saveUser(user: User)
    suspend fun getCurrentUser(): User
    suspend fun getUserById(userId: String): Flow<User?>
    suspend fun markMovieAsWatched(userId: String, movie: MovieDto)
    suspend fun getWatchedMovies(userId: String): Flow<List<Movie>>
}