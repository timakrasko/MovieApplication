package ua.edu.sumdu.movielibrary.presentation.user_profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import ua.edu.sumdu.movielibrary.data.repository.UserRepository
import ua.edu.sumdu.movielibrary.domain.Movie
import ua.edu.sumdu.movielibrary.domain.User

class UserProfileViewModel(
    private val userId: String?,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserProfileState())
    val uiState: StateFlow<UserProfileState> = _uiState

    init {
        loadUser()
        getWatchedMovies()
    }

    private fun loadUser() {
        viewModelScope.launch {
            val userFlow: Flow<User?>
            if (userId.isNullOrEmpty()) {
                userFlow = userRepository.getCurrentUser()
                _uiState.value = _uiState.value.copy(isCurrentAuthorizedUser = true)
            } else {
                userFlow = userRepository.getUserById(userId)
            }

            userFlow.collect { userData ->
                _uiState.value = _uiState.value.copy(
                    user = userData)
                getWatchedMovies()
            }
        }
    }


    private fun getWatchedMovies() {
        viewModelScope.launch {
            if (_uiState.value.user?.uid  != null) {
                userRepository.getWatchedMovies(_uiState.value.user!!.uid)
                    .onStart {
                        _uiState.value = _uiState.value.copy(isLoading = true)
                    }
                    .catch { e ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = e.message
                        )
                    }
                    .collect { movies ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            movies = movies,
                            errorMessage = null
                        )
                    }
            }
        }
    }
}

data class UserProfileState(
    val isCurrentAuthorizedUser: Boolean = false,
    val user: User? = null,
    val movies: List<Movie> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

