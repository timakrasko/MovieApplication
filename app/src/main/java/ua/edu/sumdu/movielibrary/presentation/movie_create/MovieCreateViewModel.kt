package ua.edu.sumdu.movielibrary.presentation.movie_create

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ua.edu.sumdu.movielibrary.data.repository.MovieRepository
import ua.edu.sumdu.movielibrary.domain.Genre
import ua.edu.sumdu.movielibrary.domain.Movie

class MovieCreateViewModel(
    private val repository: MovieRepository
): ViewModel() {
    private val _state = MutableStateFlow(MovieCreateState())
    val state: StateFlow<MovieCreateState> = _state

    private val _scrollState = MutableStateFlow(0)
    val scrollState: StateFlow<Int> = _scrollState

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

    suspend fun createMovie() {
        val currentState = _state.value
        if (currentState.title.isBlank() || currentState.releaseYear.isBlank()) {
            _state.value = currentState.copy(errorMessage = "Title and Release Year are required")
            return
        }
//        _state.value = currentState.copy(isLoading = true, errorMessage = null)
            try {
                val img = currentState.imageUri?.let { repository.uploadImageToStorage(it) }
                val movie = Movie(
                    title = currentState.title,
                    director = currentState.director,
                    description = currentState.description,
                    releaseYear = currentState.releaseYear,
                    imageUrl = img.toString(),
                    genres = currentState.selectedGenres.map { it.name }
                )
                repository.addMovie(movie)
                _state.value = currentState.copy(
                    isLoading = false,
                    title = "",
                    description = "",
                    releaseYear = "",
                    director = "",
                    imageUri = null,
                    selectedGenres = emptyList()
                )
            } catch (e: Exception) {
                _state.value = currentState.copy(
                    isLoading = false,
                    errorMessage = "Failed to create movie: ${e.localizedMessage}"
                )
            } finally {
                _state.value = currentState.copy(isLoading = false)
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