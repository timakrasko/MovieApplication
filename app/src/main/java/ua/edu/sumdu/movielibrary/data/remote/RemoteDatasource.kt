package ua.edu.sumdu.movielibrary.data.remote

class RemoteDatasource(private val movieService: MovieService) {
    suspend fun getMovieById(id: String, key: String) = movieService.getMovieById(id, key)
}