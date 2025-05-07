package ua.edu.sumdu.movielibrary.presentation.movie_update

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.edu.sumdu.movielibrary.data.repository.MovieRepository
import ua.edu.sumdu.movielibrary.domain.Genre
import ua.edu.sumdu.movielibrary.domain.Movie

class MovieUpdateViewModel(
    private val movieId: String,
    private val movieRepository: MovieRepository,
): ViewModel() {
    private val _state = MutableStateFlow(MovieUpdateState())
    val state: StateFlow<MovieUpdateState> = _state

    private val _scrollState = MutableStateFlow(0)
    val scrollState: StateFlow<Int> = _scrollState

    init {
        getCurrentMovie()
    }
    private fun getCurrentMovie() {
        viewModelScope.launch {
            movieRepository.getMovieById(movieId).collect { movieData ->
                _state.value = _state.value.copy(
                    title = movieData?.title.orEmpty(),
                    director = movieData?.director.orEmpty(),
                    description = movieData?.description.orEmpty(),
                    releaseYear = movieData?.releaseYear.orEmpty(),
                    selectedGenres = movieData?.genres?.mapNotNull { genreName ->
                        Genre.values().find { it.name.equals(genreName, ignoreCase = true) }
                    } ?: emptyList(),
                    initialImageUrl = movieData?.imageUrl
                )
            }
        }
    }

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
        viewModelScope.launch {
            _state.update { it.copy(imageUri = newImageUri) }
        }
    }

    fun onGenreSelected(genre: Genre) {
        val currentGenres = _state.value.selectedGenres
        _state.value = if (currentGenres.contains(genre)) {
            _state.value.copy(selectedGenres = currentGenres - genre)
        } else {
            _state.value.copy(selectedGenres = currentGenres + genre)
        }
    }

    fun updateMovie() {
        viewModelScope.launch {
            val currentState = state.value

            try {
                val uploadedImageUrl = if (currentState.imageUri != null) {
                    movieRepository.uploadImageToStorage(currentState.imageUri)
                } else {
                    currentState.initialImageUrl.orEmpty()
                }

                val updatedMovie = Movie(
                    title = currentState.title,
                    director = currentState.director,
                    description = currentState.description,
                    releaseYear = currentState.releaseYear,
                    imageUrl = uploadedImageUrl,
                    genres = currentState.selectedGenres.map { it.name }
                )

                movieRepository.updateMovie(movieId, updatedMovie)

                _state.value = currentState.copy(isLoading = false, updateCompleted = true)
            } catch (e: Exception) {
                _state.value = currentState.copy(
                    isLoading = false,
                    errorMessage = "Failed to update movie: ${e.localizedMessage}"
                )
            }
        }
    }
}

data class MovieUpdateState(
    val title: String = "",
    val director: String = "",
    val description: String = "",
    val releaseYear: String = "",
    val selectedGenres: List<Genre> = emptyList(),
    val imageUri: Uri? = null,
    val initialImageUrl: String? = null,
    val updateCompleted: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)