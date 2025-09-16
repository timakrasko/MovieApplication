package ua.edu.sumdu.movielibrary.data.remote

sealed class NetworkResult<T>(
    val data: T? = null,
    message: String? = null
) {
    class Success<T>(data: T): NetworkResult<T>(data = data)
    class Error<T>(data: T?, message: String?): NetworkResult<T>(data = data, message = message)
    class Loading<T>(): NetworkResult<T>()
}