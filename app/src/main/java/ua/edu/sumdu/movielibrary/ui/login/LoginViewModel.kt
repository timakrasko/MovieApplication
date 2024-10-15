package ua.edu.sumdu.movielibrary.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import ua.edu.sumdu.movielibrary.data.MainScreenDataObject
import ua.edu.sumdu.movielibrary.repository.OnlineMovieRepository

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val error: String = ""
)

class LoginViewModel(private  val repository: OnlineMovieRepository = OnlineMovieRepository()) : ViewModel() {
    var state by mutableStateOf(LoginUiState())
        private set

    fun setEmail(email: String){
        this.state = state.copy(email = email)
    }

    fun setPassword(password: String){
        this.state = state.copy(password = password)
    }

    fun signIn(onSignInSuccess: (MainScreenDataObject) -> Unit) {
        repository.signIn(
            state.email,
            state.password,
            onSignInSuccess = { navData ->
                onSignInSuccess(navData)
                this.state = state.copy(error = "")
            },
            onSignInFailure = { error ->
                this.state = state.copy(error = error)
            }
        )
    }

    fun signUp(onSignUpSuccess: (MainScreenDataObject) -> Unit) {
        repository.signUp(
            state.email,
            state.password,
            onSignUpSuccess = { navData ->
                onSignUpSuccess(navData)
                this.state = state.copy(error = "")
            },
            onSignUpFailure = { error ->
                this.state = state.copy(error = error)
            }
        )
    }

    companion object {

        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val application = checkNotNull(extras[APPLICATION_KEY])
                val savedStateHandle = extras.createSavedStateHandle()

                return LoginViewModel(
                    OnlineMovieRepository()
                ) as T
            }
        }
    }
}