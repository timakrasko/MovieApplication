package ua.edu.sumdu.movielibrary.presentation.movie_create

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ua.edu.sumdu.movielibrary.data.Dto.MovieRepository
import ua.edu.sumdu.movielibrary.domain.Genre

class MovieCreateViewModel(
    private val repository: MovieRepository
): ViewModel() {
    private val _state = MutableStateFlow(MovieCreateState())
    val state: StateFlow<MovieCreateState> = _state

    fun onTitleChange(newTitle: String) {
        _state.value = _state.value.copy(title = newTitle)
    }

    fun onDirectorChange(newDirector: String) {
        _state.value = _state.value.copy(director = newDirector)
    }

    fun onDescriptionChange(newDescription: String) {
        _state.value = _state.value.copy(description = newDescription)
    }

    fun onReleaseYearChange(newReleaseYear: String) {
        _state.value = _state.value.copy(releaseYear = newReleaseYear)
    }

    fun onImageSelected(newImageUri: Uri) {
        _state.value = _state.value.copy(imageUri = newImageUri)
    }

    fun onGenreSelected(genre: Genre) {
        val currentGenres = _state.value.selectedGenres
        _state.value = if (currentGenres.contains(genre)) {
            _state.value.copy(selectedGenres = currentGenres - genre)
        } else {
            _state.value.copy(selectedGenres = currentGenres + genre)
        }
    }
}

data class MovieCreateState(
    val title: String = "",
    val director: String = "",
    val description: String = "",
    val releaseYear: String = "",
    val selectedGenres: List<Genre> = emptyList(),
    val imageUri: Uri? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)