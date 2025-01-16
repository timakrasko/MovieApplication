package ua.edu.sumdu.movielibrary.presentation.main_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ua.edu.sumdu.movielibrary.data.Dto.MovieRepository
import ua.edu.sumdu.movielibrary.domain.Movie

class MainViewModel(
    private val repository: MovieRepository
): ViewModel() {
    private val _movieListState = MutableStateFlow(MovieListState())
    val movieListState: StateFlow<MovieListState> = _movieListState


    init {
        fetchMovies()
    }

    fun fetchMovies() {
        viewModelScope.launch {
            _movieListState.value = _movieListState.value.copy(isLoading = true)

            try {
                val movies = repository.getMovies()
                _movieListState.value = MovieListState(isLoading = false, movies = movies)
            } catch (e: Exception) {
                _movieListState.value = MovieListState(isLoading = false, errorMessage = e.message)
            }
        }
    }

    fun addMovie(movie: Movie) {
        viewModelScope.launch {
            repository.addMovie(movie)
            fetchMovies()
        }
    }
}

data class MovieListState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val movies: List<Movie> = emptyList()
)