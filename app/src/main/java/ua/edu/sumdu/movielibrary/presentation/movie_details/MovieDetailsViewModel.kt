package ua.edu.sumdu.movielibrary.presentation.movie_details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ua.edu.sumdu.movielibrary.data.Dto.MovieDto
import ua.edu.sumdu.movielibrary.data.Dto.MovieRepository
import ua.edu.sumdu.movielibrary.domain.Movie

class MovieDetailsViewModel(
    private val movie: MovieDto,
    private val repository: MovieRepository,
): ViewModel() {
    fun deleteMovie() {
        viewModelScope.launch {
            try {
                repository.deleteMovie(movie.id, movie.imageUrl)
                Log.d("21223", "12223")
            } catch (e: Exception) {
                Log.d("213", "123")
            }
        }
    }
    fun markMovieAsWatched(userId: String, movie: MovieDto) {
        viewModelScope.launch {
            try {
                repository.markMovieAsWatched(userId, movie)
            } catch (e: Exception) {
                Log.e("MovieViewModel", "Error marking movie as watched: ${e.message}")
            }
        }
    }
}