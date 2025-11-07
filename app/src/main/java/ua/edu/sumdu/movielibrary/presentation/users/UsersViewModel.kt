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
import java.util.Locale

class UsersViewModel(
    private val repository: UserRepository
): ViewModel() {
    private val _userListState = MutableStateFlow(UserListState())
    val userListState: StateFlow<UserListState> = _userListState

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    init {
        observeUsers()
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        filterUsers()
    }

    private fun filterUsers() {
        val currentUsers = _userListState.value.allUsers
        val query = _searchQuery.value.lowercase(Locale.getDefault())

        val filteredUsers = if (query.isBlank()) {
            currentUsers
        } else {
            currentUsers.filter {
                it.username.lowercase(Locale.getDefault()).contains(query)
            }
        }

        _userListState.value = _userListState.value.copy(
            users = filteredUsers
        )
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
                        allUsers = users,
                        errorMessage = null
                    )
                }
        }
    }
}

data class UserListState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val users: List<User> = emptyList(),
    val allUsers: List<User> = emptyList()
)