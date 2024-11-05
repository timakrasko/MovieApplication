package ua.edu.sumdu.movielibrary.presentation.login

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ua.edu.sumdu.movielibrary.data.Dto.MainScreenDataObject
import ua.edu.sumdu.movielibrary.data.OnlineMovieRepository

data class LoginUiState(
    val email: String = "timakrasko2004@gmail.com",
    val password: String = "123456789",
    val error: String = ""
)

class LoginViewModel(private  val repository: OnlineMovieRepository = OnlineMovieRepository()) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun setEmail(email: String){
        _uiState.update {
            it.copy(email = email)
        }
    }

    fun setPassword(password: String){
        _uiState.update {
            it.copy(password = password)
        }
    }

    fun signIn(onSignInSuccess: (MainScreenDataObject) -> Unit) {
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

    fun signUp(onSignUpSuccess: (MainScreenDataObject) -> Unit) {
        repository.signUp(
            _uiState.value.email,
            _uiState.value.password,
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

//    companion object {
//
//        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
//            @Suppress("UNCHECKED_CAST")
//            override fun <T : ViewModel> create(
//                modelClass: Class<T>,
//                extras: CreationExtras
//            ): T {
//                val application = checkNotNull(extras[APPLICATION_KEY])
//                val savedStateHandle = extras.createSavedStateHandle()
//
//                return LoginViewModel(
//                    OnlineMovieRepository()
//                ) as T
//            }
//        }
//    }
}