package ua.edu.sumdu.movielibrary.data.remote


import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ua.edu.sumdu.movielibrary.data.dto.MovieResponse

interface MovieService {
    @GET(".")
    suspend fun getMovieById(
        @Query("i") imdbId: String,
        @Query("apikey") apiKey: String
    ): Response<MovieResponse>
}