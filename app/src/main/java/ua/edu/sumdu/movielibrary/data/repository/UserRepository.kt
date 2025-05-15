package ua.edu.sumdu.movielibrary.data.repository

import kotlinx.coroutines.flow.Flow
import ua.edu.sumdu.movielibrary.domain.Movie
import ua.edu.sumdu.movielibrary.domain.User

interface UserRepository {
    suspend fun saveUser(user: User)
    suspend fun getUsers(): Flow<List<User>>
    suspend fun getCurrentUserId(): String
    suspend fun getUserById(userId: String): Flow<User?>
    suspend fun markMovieAsWatched(userId: String, movie: Movie)
    suspend fun getWatchedMovies(userId: String): Flow<List<Movie>>
    suspend fun markMovieAsPlaned(userId: String, movie: Movie)
    suspend fun getPlanedMovies(userId: String): Flow<List<Movie>>
    suspend fun removeWatchedMovie(userId: String, movieId: String)
    suspend fun removePlanedMovie(userId: String, movieId: String)
    suspend fun removeMovieFromAllUsers(movieId: String)
    suspend fun addUserToFriends(friendId: String)
    suspend fun getFriends(userId: String): Flow<List<User>>
    suspend fun rateMovie(userId: String, movieId: String, rating: Int)
}