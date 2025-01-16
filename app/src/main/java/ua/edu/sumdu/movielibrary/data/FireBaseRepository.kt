package ua.edu.sumdu.movielibrary.data

import android.net.Uri
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import kotlinx.coroutines.tasks.await
import ua.edu.sumdu.movielibrary.data.Dto.MovieRepository
import ua.edu.sumdu.movielibrary.domain.Movie
import java.util.UUID


class FireBaseRepository : MovieRepository{

    private val dataBase = Firebase.firestore.collection("movies")
    private val storage = Firebase.storage

    override suspend fun getMovies(): List<Movie> {
        return try {
            dataBase.get().await().documents.mapNotNull { document ->
                document.toObject(Movie::class.java)?.copy(id = document.id) // Add document ID
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun addMovie(movie: Movie) {
        dataBase.add(movie).await()
    }

    override suspend fun deleteMovie(id: String, imageUrl: String?) {
        try {
            dataBase.document(id).delete().await()
            if (!imageUrl.isNullOrEmpty()) {
                deleteImageFromStorage(imageUrl)
            }
        } catch (e: Exception) {
            throw Exception("Failed to delete movie: ${e.localizedMessage}")
        }
    }

    override suspend fun uploadImageToStorage(uri: Uri): String {
        val storageReference = storage.reference
        val imageRef = storageReference.child("movie_images/${UUID.randomUUID()}.jpg")

        val uploadTask = imageRef.putFile(uri)
        uploadTask.await()

        val downloadUrl = imageRef.downloadUrl.await()
        return downloadUrl.toString()
    }

    private suspend fun deleteImageFromStorage(imageUrl: String) {
        val storageRef = storage.getReferenceFromUrl(imageUrl)
        storageRef.delete().await()
    }
}