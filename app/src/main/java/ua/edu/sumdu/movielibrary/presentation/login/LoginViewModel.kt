package ua.edu.sumdu.movielibrary.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.edu.sumdu.movielibrary.data.dto.MainScreenDataObject
import ua.edu.sumdu.movielibrary.data.repository.OnlineMovieRepository


class LoginViewModel(
    private val repository: OnlineMovieRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun setEmail(email: String) {
        _uiState.update {
            it.copy(email = email)
        }
    }

    fun setPassword(password: String) {
        _uiState.update {
            it.copy(password = password)
        }
    }

    fun setUsername(username: String) {
        _uiState.update {
            it.copy(username = username)
        }
    }

    fun isUserSignedIn(): Boolean {
        return repository.isSignedIn()
    }

    fun signIn(onSignInSuccess: (MainScreenDataObject) -> Unit) {
        viewModelScope.launch {
            repository.signIn(
                _uiState.value.email,
                _uiState.value.password,
                onSignInSuccess = { navData ->
                    onSignInSuccess(navData)
                    _uiState.update {
                        it.copy(error = "")
                    }
                },
                onSignInFailure = { error ->
                    _uiState.update {
                        it.copy(error = error)
                    }
                }
            )
        }
    }

    fun signUp(onSignUpSuccess: (MainScreenDataObject) -> Unit) {
        viewModelScope.launch {
            repository.signUp(
                _uiState.value.email,
                _uiState.value.password,
                _uiState.value.username,
                onSignUpSuccess = { navData ->
                    onSignUpSuccess(navData)
                    _uiState.update {
                        it.copy(error = "")
                    }
                },
                onSignUpFailure = { error ->
                    _uiState.update {
                        it.copy(error = error)
                    }
                }
            )
        }
    }
}

data class LoginUiState(
    val email: String = "timakrasko2004@gmail.com",
    val password: String = "123456789",
    val error: String = "",
    val username: String = "",
    val isLoading: Boolean = false,
    val isSignedIn: Boolean = false
)