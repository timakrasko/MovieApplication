package ua.edu.sumdu.movielibrary.data.repository

import ua.edu.sumdu.movielibrary.data.dto.MovieResponse
import ua.edu.sumdu.movielibrary.data.remote.BaseApiResponse
import ua.edu.sumdu.movielibrary.data.remote.NetworkResult
import ua.edu.sumdu.movielibrary.data.remote.RemoteDatasource

class ImdbRepository(
    private val remoteDatasource: RemoteDatasource
): BaseApiResponse() {
    suspend fun getMovieById(id: String, key: String): NetworkResult<MovieResponse> {
        return safeApiCall { remoteDatasource.getMovieById(id, key) }
    }
}