package ua.edu.sumdu.movielibrary.data.repository

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import ua.edu.sumdu.movielibrary.data.Dto.MovieDto
import ua.edu.sumdu.movielibrary.domain.Movie
import ua.edu.sumdu.movielibrary.domain.User

class FireBaseUserRepository(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
): UserRepository {
    override suspend fun saveUser(user: User) {
        firestore.collection("users").document(user.uid).set(user).await()
    }

    override suspend fun getCurrentUser(): User {
        return TODO()
    }

    override suspend fun getUserById(userId: String): Flow<User?> = callbackFlow {
        val userRef = firestore.collection("users").document(userId)

        val listener = userRef.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                close(exception)
                return@addSnapshotListener
            }

            val user = snapshot?.toObject(User::class.java)
            trySend(user)
        }

        awaitClose { listener.remove() }
    }

    override suspend fun markMovieAsWatched(userId: String, movie: MovieDto) {
        val userWatchedRef = Firebase.firestore.collection("users")
            .document(userId)
            .collection("watched_movies")
            .document(movie.id)

        try {
            userWatchedRef.set(movie).await()
        } catch (e: Exception) {
            throw Exception("Failed to mark movie as watched: ${e.localizedMessage}")
        }
    }

    override suspend fun getWatchedMovies(userId: String): Flow<List<Movie>> {
        return callbackFlow {
            val watchedMoviesRef = Firebase.firestore.collection("users").document(userId)
                .collection("watched_movies")

            val listenerRegistration = watchedMoviesRef.addSnapshotListener { snapshot, exception ->
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
}