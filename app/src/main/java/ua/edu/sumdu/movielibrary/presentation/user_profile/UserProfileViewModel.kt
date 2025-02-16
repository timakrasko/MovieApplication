package ua.edu.sumdu.movielibrary.presentation.user_profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import ua.edu.sumdu.movielibrary.data.repository.UserRepository
import ua.edu.sumdu.movielibrary.domain.User
import ua.edu.sumdu.movielibrary.presentation.main_screen.MovieListState

class UserProfileViewModel(
    private val userId: String?,
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val _movieListState = MutableStateFlow(MovieListState())
    val movieListState: StateFlow<MovieListState> = _movieListState

    init {
        loadUser()
        getWatchedMovies()
    }

    private fun loadUser() {
        viewModelScope.launch {
            val userFlow = if (userId.isNullOrEmpty()) {
                userRepository.getCurrentUser()
            } else {
                userRepository.getUserById(userId)
            }

            userFlow.collect { userData ->
                _user.value = userData
                getWatchedMovies() // Call after user is loaded
            }
        }
    }

//    private fun loadUser() {
//        viewModelScope.launch {
//            if (userId.isNullOrEmpty()) {
//                userRepository.getCurrentUser().collect { userData ->
//                    _user.value = userData
//                }
//            } else {
//                userRepository.getUserById(userId).collect { userData ->
//                    _user.value = userData
//                }
//            }
//        }
//    }

    private fun getWatchedMovies() {
        viewModelScope.launch {
            if (userId != null) {
                userRepository.getWatchedMovies(userId)
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
}