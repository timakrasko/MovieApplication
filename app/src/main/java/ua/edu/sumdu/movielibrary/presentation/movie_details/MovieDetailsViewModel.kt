package ua.edu.sumdu.movielibrary.presentation.movie_details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import ua.edu.sumdu.movielibrary.data.repository.MovieRepository
import ua.edu.sumdu.movielibrary.data.repository.UserRepository
import ua.edu.sumdu.movielibrary.domain.Movie

class MovieDetailsViewModel(
    private val movieId: String,
    private val movieRepository: MovieRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MovieDetailsState())
    val uiState: StateFlow<MovieDetailsState> = _uiState

    init {
        getCurrentMovie()
        getCurrentUser()
    }

    private fun getCurrentMovie() {
        viewModelScope.launch {
            val movieFlow = movieRepository.getMovieById(movieId)
            movieFlow.collect { movieData ->
                _uiState.value = _uiState.value.copy(
                    movie = movieData,
                )
            }

        }
    }

    private fun getCurrentUser() {
        viewModelScope.launch {
            val userId = userRepository.getCurrentUserId()
            val userFlow = userRepository.getUserById(userId)
            userFlow.collect { userData ->
                _uiState.value = _uiState.value.copy(
                    currentUserId = userData?.uid,
                    isAdmin = userData?.isAdmin == true,
                )
                getWatchedAndPlanedMovies()
            }
        }
    }

    fun deleteMovie() {
        viewModelScope.launch {
            try {
                movieRepository.deleteMovie(
                    _uiState.value.movie!!.id,
                    _uiState.value.movie!!.imageUrl
                )
            } catch (e: Exception) {
                Log.e("MovieViewModel", "Error loading movie: ${e.message}")
            }
        }
    }

    fun markMovieAsWatched() {
        viewModelScope.launch {
            try {
                userRepository.markMovieAsWatched(
                    _uiState.value.currentUserId!!,
                    _uiState.value.movie!!
                )
                userRepository.removePlanedMovie(
                    _uiState.value.currentUserId!!,
                    _uiState.value.movie!!.id
                )
            } catch (e: Exception) {
                Log.e("MovieViewModel", "Error marking movie as watched: ${e.message}")
            }
        }
    }

    fun rateMovie(rating: Int) {
        viewModelScope.launch {
            try {
                userRepository.rateMovie(
                    _uiState.value.currentUserId!!,
                    _uiState.value.movie!!.id,
                    rating
                )
            } catch (e: Exception) {
                Log.e("MovieViewModel", "Error rating movie: ${e.message}")
            }
            getWatchedAndPlanedMovies()
        }
    }

    fun markMovieAsPlaned() {
        viewModelScope.launch {
            try {
                userRepository.markMovieAsPlaned(
                    _uiState.value.currentUserId!!,
                    _uiState.value.movie!!
                )
            } catch (e: Exception) {
                Log.e("MovieViewModel", "Error marking movie as planed: ${e.message}")
            }
        }
    }

    fun removeWatchedMovie() {
        viewModelScope.launch {
            try {
                userRepository.removeWatchedMovie(
                    _uiState.value.currentUserId!!,
                    _uiState.value.movie!!.id
                )
            } catch (e: Exception) {
                Log.e("MovieViewModel", "Error remove movie from watched: ${e.message}")
            }
        }
    }

    fun removePlanedMovie() {
        viewModelScope.launch {
            try {
                userRepository.removePlanedMovie(
                    _uiState.value.currentUserId!!,
                    _uiState.value.movie!!.id
                )
            } catch (e: Exception) {
                Log.e("MovieViewModel", "Error remove movie from planed: ${e.message}")
            }
        }
    }

    private fun getWatchedAndPlanedMovies() {
        viewModelScope.launch {
            val userId = _uiState.value.currentUserId
            if (userId != null) {
                userRepository.getWatchedMovies(userId)
                    .combine(userRepository.getPlanedMovies(userId)) { watched, planned ->
                        val watchedMovie = watched.firstOrNull { it.id == _uiState.value.movie?.id }

                        _uiState.value.copy(
                            isLoading = false,
                            isWatched = watchedMovie != null,
                            isPlanned = planned.any { it.id == _uiState.value.movie?.id },
                            movieRating = watchedMovie?.rating, // Додаємо рейтинг
                            errorMessage = null
                        )
                    }
                    .onStart { _uiState.value = _uiState.value.copy(isLoading = true) }
                    .catch { e ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = e.message
                        )
                    }
                    .collect { newState ->
                        _uiState.value = newState
                    }
            }
        }
    }
}

data class MovieDetailsState(
    val movie: Movie? = null,
    val movieRating: Int? = null,
    val currentUserId: String? = null,
    val isAdmin: Boolean = false,
    val isWatched: Boolean = false,
    val isPlanned: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)