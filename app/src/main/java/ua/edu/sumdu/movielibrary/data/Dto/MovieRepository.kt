package ua.edu.sumdu.movielibrary.data.Dto

import android.net.Uri
import ua.edu.sumdu.movielibrary.domain.Movie

interface MovieRepository {
    suspend fun getMovies(): List<Movie>
    suspend fun addMovie(movie: Movie)
    suspend fun deleteMovie(id: String)
    suspend fun uploadImageToStorage(uri: Uri): String
}