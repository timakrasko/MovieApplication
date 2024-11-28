package ua.edu.sumdu.movielibrary.presentation.main_screen

import android.content.Context
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ua.edu.sumdu.movielibrary.data.FireBaseRepository
import ua.edu.sumdu.movielibrary.domain.Movie

class MainViewModel(
    private val repository: FireBaseRepository = FireBaseRepository()
): ViewModel() {
    private val _movieList = MutableStateFlow<List<Movie>>(emptyList())
    val movieList: StateFlow<List<Movie>> = _movieList

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    init {
        fetchMovies()
    }

    fun fetchMovies() {
        repository.getMovies().addSnapshotListener { snapshot, _ ->
            _movieList.value = snapshot?.toObjects(Movie::class.java) ?: emptyList()
        }
    }

    fun addMovie(context: Context) {
        val task = repository.getImageTask(context)
        task.addOnSuccessListener { uploadTask ->
            uploadTask.metadata?.reference?.downloadUrl?.addOnCompleteListener { uriTask ->
                repository.saveMovie(
                    Movie(
                        "The Lord of The Rings",
                        "desc1",
                        uriTask.result.toString(),
                        "Piter Jackson",
                        listOf("Fantasy", "Drama", "Action")
                    )
                )
            }
        }
    }
}

data class UiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)