package ua.edu.sumdu.movielibrary.data.repository

import kotlinx.coroutines.flow.Flow
import ua.edu.sumdu.movielibrary.domain.Movie
import ua.edu.sumdu.movielibrary.domain.User

interface UserRepository {
    suspend fun saveUser(user: User)
    suspend fun getUsers(): Flow<List<User>>
    suspend fun getCurrentUser(): Flow<User?>
    suspend fun getUserById(userId: String): Flow<User?>
    suspend fun markMovieAsWatched(userId: String, movie: Movie)
    suspend fun getWatchedMovies(userId: String): Flow<List<Movie>>
    suspend fun markMovieAsPlaned(userId: String, movie: Movie)
    suspend fun getPlanedMovies(userId: String): Flow<List<Movie>>
    suspend fun removeWatchedMovie(userId: String, movieId: String)
    suspend fun removePlanedMovie(userId: String, movieId: String)
    suspend fun removeMovieFromAllUsers(movieId: String)
}