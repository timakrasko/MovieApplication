package ua.edu.sumdu.movielibrary.presentation.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import ua.edu.sumdu.movielibrary.data.repository.UserRepository
import ua.edu.sumdu.movielibrary.domain.User

class UsersViewModel(
    private val repository: UserRepository
): ViewModel() {
    private val _userListState = MutableStateFlow(UserListState())
    val userListState: StateFlow<UserListState> = _userListState

    init {
        observeUsers()
    }

    private fun observeUsers() {
        viewModelScope.launch {
            repository.getUsers()
                .onStart {
                    _userListState.value = _userListState.value.copy(isLoading = true)
                }
                .catch { e ->
                    _userListState.value = _userListState.value.copy(
                        isLoading = false,
                        errorMessage = e.message
                    )
                }
                .collect { users ->
                    _userListState.value = _userListState.value.copy(
                        isLoading = false,
                        users = users,
                        errorMessage = null
                    )
                }
        }
    }
}

data class UserListState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val users: List<User> = emptyList()
)