package ua.edu.sumdu.movielibrary.data.Dto

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import ua.edu.sumdu.movielibrary.domain.Movie

interface MovieRepository {
    suspend fun getMovies(): Flow<List<Movie>>
    suspend fun addMovie(movie: Movie)
    suspend fun deleteMovie(id: String, imageUrl: String?)
    suspend fun uploadImageToStorage(uri: Uri): String
}