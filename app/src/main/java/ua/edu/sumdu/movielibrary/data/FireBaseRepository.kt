package ua.edu.sumdu.movielibrary.data

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import ua.edu.sumdu.movielibrary.data.Dto.MovieRepository
import ua.edu.sumdu.movielibrary.domain.Movie


class FireBaseRepository : MovieRepository{

    private val dataBase = Firebase.firestore.collection("movies")

    override suspend fun getMovies(): List<Movie> {
        return try {
            dataBase.get().await().documents.map { it.toObject(Movie::class.java)!! }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun addMovie(movie: Movie) {
        dataBase.add(movie).await()
    }

    override suspend fun deleteMovie(id: String) {
        dataBase.whereEqualTo("id", id).get().await().documents.forEach {
            it.reference.delete().await()
        }
    }

}