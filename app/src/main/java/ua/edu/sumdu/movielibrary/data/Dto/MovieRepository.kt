package ua.edu.sumdu.movielibrary.data.Dto

import ua.edu.sumdu.movielibrary.domain.Movie

interface MovieRepository {
    suspend fun getMovies(): List<Movie>
    suspend fun addMovie(movie: Movie)
    suspend fun deleteMovie(id: String)
}