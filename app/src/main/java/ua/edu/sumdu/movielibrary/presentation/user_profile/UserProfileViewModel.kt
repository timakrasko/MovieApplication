package ua.edu.sumdu.movielibrary.presentation.user_profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
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
    }

    private fun loadUser() {
        viewModelScope.launch {
            val userFlow: Flow<User?>
            if (userId.isNullOrEmpty()) {
                val currentUserId = userRepository.getCurrentUserId()
                userFlow = userRepository.getUserById(currentUserId)
                _uiState.value = _uiState.value.copy(isCurrentAuthorizedUser = true)
            } else {
                userFlow = userRepository.getUserById(userId)
            }

            userFlow.collect { userData ->
                _uiState.value = _uiState.value.copy(
                    user = userData
                )
                getWatchedAndPlanedMovies()
                loadFriends()
            }
        }
    }


    private fun getWatchedAndPlanedMovies() {
        viewModelScope.launch {
            val userId = _uiState.value.user?.uid ?: return@launch

            userRepository.getWatchedMovies(userId)
                .combine(userRepository.getPlanedMovies(userId)) { watched, planned ->
                    watched to planned
                }
                .onStart { _uiState.value = _uiState.value.copy(isLoading = true) }
                .catch { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = e.message
                    )
                }
                .collect { (watchedMovies, plannedMovies) ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        watchedMovies = watchedMovies,
                        planedMovies = plannedMovies,
                        errorMessage = null
                    )
                }
        }
    }

    fun loadFriends() {
        viewModelScope.launch {
            val profileUserId = _uiState.value.user?.uid ?: return@launch
            val currentUserId = userRepository.getCurrentUserId()

            userRepository.getFriends(profileUserId)
                .onStart {
                    _uiState.value = _uiState.value.copy(isLoading = true)
                }
                .catch { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = e.message
                    )
                }
                .collect { friends ->
                    val currentUserFriends = userRepository.getFriends(currentUserId).firstOrNull() ?: emptyList()
                    val isFriend = currentUserFriends.any { it.uid == profileUserId }

                    val isOwnProfile = profileUserId == currentUserId

                    _uiState.value = _uiState.value.copy(
                        isFriend = isFriend,
                        isLoading = false,
                        friends = friends,
                        showFriendActionButton = !isOwnProfile,
                        errorMessage = null
                    )
                }
        }
    }

    fun toggleFriendship(friendId: String) {
        viewModelScope.launch {
            try {
                if (_uiState.value.isFriend) {
                    userRepository.removeUserFromFriends(friendId)
                } else {
                    userRepository.addUserToFriends(friendId)
                }
                val currentUserId = userRepository.getCurrentUserId()

                val isOwnProfile = friendId == currentUserId

                _uiState.value = _uiState.value.copy(
                    isFriend = !_uiState.value.isFriend,
                    showFriendActionButton = !isOwnProfile,
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.localizedMessage,
                )
            }
        }
    }
}

data class UserProfileState(
    val isCurrentAuthorizedUser: Boolean = false,
    val isFriend: Boolean = false,
    val user: User? = null,
    val watchedMovies: List<Movie> = emptyList(),
    val planedMovies: List<Movie> = emptyList(),
    val friends: List<User> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val showFriendActionButton: Boolean = false
)

