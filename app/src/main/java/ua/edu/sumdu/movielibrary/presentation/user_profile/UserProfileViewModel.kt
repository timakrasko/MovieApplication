package ua.edu.sumdu.movielibrary.presentation.user_profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import ua.edu.sumdu.movielibrary.data.Dto.MovieDto
import ua.edu.sumdu.movielibrary.data.Dto.MovieRepository
import ua.edu.sumdu.movielibrary.domain.Movie
import ua.edu.sumdu.movielibrary.presentation.main_screen.MovieListState

class UserProfileViewModel(
    private val user: FirebaseUser,
    private val repository: MovieRepository,
): ViewModel() {
    private val _movieListState = MutableStateFlow(MovieListState())
    val movieListState: StateFlow<MovieListState> = _movieListState
    init {
        getWatchedMovies()
    }

    fun getWatchedMovies() {
        viewModelScope.launch {
            repository.getWatchedMovies(userId = user.uid)
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