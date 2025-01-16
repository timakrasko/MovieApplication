package ua.edu.sumdu.movielibrary.presentation.movie_details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ua.edu.sumdu.movielibrary.data.Dto.MovieDto
import ua.edu.sumdu.movielibrary.data.Dto.MovieRepository

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
}