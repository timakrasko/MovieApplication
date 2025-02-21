package ua.edu.sumdu.movielibrary.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import ua.edu.sumdu.movielibrary.data.dto.MovieDto
import ua.edu.sumdu.movielibrary.domain.Movie
import ua.edu.sumdu.movielibrary.domain.User

class FireBaseUserRepository(
    private val auth: FirebaseAuth,
    firestore: FirebaseFirestore
): UserRepository {

    private val userCollection = firestore.collection("users")

    override suspend fun saveUser(user: User) {
        userCollection.document(user.uid).set(user).await()
    }

    override suspend fun getCurrentUser(): Flow<User?> {
        return getUserById(auth.currentUser!!.uid)
    }

    override suspend fun getUserById(userId: String): Flow<User?> = callbackFlow {
        val userRef = userCollection.document(userId)

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

    override suspend fun getUsers(): Flow<List<User>> {
        return callbackFlow {
            val listenerRegistration = userCollection.addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    close(exception)
                    return@addSnapshotListener
                }

                val users = snapshot?.documents?.mapNotNull { document ->
                    document.toObject(User::class.java)?.copy(uid = document.id)
                }.orEmpty()

                trySend(users)
            }

            awaitClose { listenerRegistration.remove() }
        }
    }

    override suspend fun getWatchedMovies(userId: String): Flow<List<Movie>> {
        return callbackFlow {
            val watchedMoviesRef = userCollection.document(userId)
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

    override suspend fun markMovieAsWatched(userId: String, movie: Movie) {
        if (userId.isBlank()) {
            throw IllegalArgumentException("User ID cannot be empty")
        }
        if (movie.id.isBlank()) {
            throw IllegalArgumentException("Movie ID cannot be empty")
        }

        val userWatchedRef = userCollection
            .document(userId)
            .collection("watched_movies")
            .document(movie.id)

        try {
            userWatchedRef.set(movie).await()
        } catch (e: Exception) {
            throw Exception("Failed to mark movie as watched: ${e.localizedMessage}")
        }
    }

    override suspend fun markMovieAsPlaned(userId: String, movie: Movie) {
        if (userId.isBlank()) {
            throw IllegalArgumentException("User ID cannot be empty")
        }
        if (movie.id.isBlank()) {
            throw IllegalArgumentException("Movie ID cannot be empty")
        }

        val userWatchedRef = userCollection
            .document(userId)
            .collection("planed_movies")
            .document(movie.id)

        try {
            userWatchedRef.set(movie).await()
        } catch (e: Exception) {
            throw Exception("Failed to mark movie as planed: ${e.localizedMessage}")
        }
    }

    override suspend fun getPlanedMovies(userId: String): Flow<List<Movie>> {
        return callbackFlow {
            val planedMoviesRef = userCollection.document(userId)
                .collection("planed_movies")

            val listenerRegistration = planedMoviesRef.addSnapshotListener { snapshot, exception ->
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