package ua.edu.sumdu.movielibrary.data.repository

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import ua.edu.sumdu.movielibrary.domain.Movie
import java.util.UUID


class FireBaseMovieRepository(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : MovieRepository {

    private val movieCollection = firestore.collection("movies")

    override suspend fun getMovies(): Flow<List<Movie>> {
        return callbackFlow {
            val listenerRegistration = movieCollection.addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    close(exception)
                    return@addSnapshotListener
                }

                val movies = snapshot?.documents?.mapNotNull { document ->
                    document.toObject(Movie::class.java)?.copy(id = document.id)
                }.orEmpty()

                trySend(movies)
            }

            awaitClose { listenerRegistration.remove() }
        }
    }

    override suspend fun addMovie(movie: Movie) {
        movieCollection.add(movie).await()
    }

    override suspend fun deleteMovie(id: String, imageUrl: String?) {
        try {
            movieCollection.document(id).delete().await()
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