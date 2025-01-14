package ua.edu.sumdu.movielibrary.presentation.movie_details

import androidx.lifecycle.ViewModel
import ua.edu.sumdu.movielibrary.data.Dto.MovieDto
import ua.edu.sumdu.movielibrary.data.Dto.MovieRepository

class MovieDetailsViewModel(
    private val movie: MovieDto,
    private val repository: MovieRepository
): ViewModel() {
    fun deleteMovie(movie: MovieDto){

    }
}