package ua.edu.sumdu.movielibrary.presentation.main_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import ua.edu.sumdu.movielibrary.data.repository.MovieRepository
import ua.edu.sumdu.movielibrary.domain.Movie

class MainViewModel(
    private val repository: MovieRepository
): ViewModel() {
    private val _movieListState = MutableStateFlow(MovieListState())
    val movieListState: StateFlow<MovieListState> = _movieListState


    init {
        observeMovies()
    }

    private fun observeMovies() {
        viewModelScope.launch {
            repository.getMovies()
                .onStart {
                    _movieListState.value = _movieListState.value.copy(isLoading = true)
                }
                .catch { e ->
                    _movieListState.value = _movieListState.value.copy(
                        isLoading = false,
                        errorMessage = e.message
                    )
                }
                .collect { movies ->
                    _movieListState.value = _movieListState.value.copy(
                        isLoading = false,
                        movies = movies,
                        errorMessage = null
                    )
                }
        }
    }
}

data class MovieListState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val movies: List<Movie> = emptyList()
)