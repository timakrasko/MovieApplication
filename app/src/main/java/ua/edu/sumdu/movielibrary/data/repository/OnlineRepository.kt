package ua.edu.sumdu.movielibrary.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import ua.edu.sumdu.movielibrary.data.dto.MainScreenDataObject

class OnlineMovieRepository(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {

    fun isSignedIn(): Boolean {
        return auth.currentUser != null
    }

    suspend fun signUp(
        email: String?,
        password: String?,
        onSignUpSuccess: (MainScreenDataObject) -> Unit,
        onSignUpFailure: (String) -> Unit
    ) {
        if (email.isNullOrBlank() || password.isNullOrBlank()) {
            onSignUpFailure("Email or password can't be empty")
            return
        }

        try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user
            if (user != null) {
                createUserInFirestoreIfNotExists(user.uid, email)
                onSignUpSuccess(MainScreenDataObject)
            } else {
                onSignUpFailure("User registration failed")
            }
        } catch (e: Exception) {
            onSignUpFailure(e.localizedMessage ?: "Sign Up Error")
        }
    }

    suspend fun signIn(
        email: String?,
        password: String?,
        onSignInSuccess: (MainScreenDataObject) -> Unit,
        onSignInFailure: (String) -> Unit
    ) {
        if (email.isNullOrBlank() || password.isNullOrBlank()) {
            onSignInFailure("Email or password can't be empty")
            return
        }

        try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val user = result.user
            if (user != null) {
                onSignInSuccess(MainScreenDataObject)
            } else {
                onSignInFailure("Authentication failed")
            }
        } catch (e: Exception) {
            onSignInFailure(e.localizedMessage ?: "Sign In Error")
        }
    }

    private suspend fun createUserInFirestoreIfNotExists(userId: String, email: String) {
        val userRef = firestore.collection("users").document(userId)

        try {
            val document = userRef.get().await()
            if (!document.exists()) {
                val user = mapOf(
                    "email" to email,
                    "uid" to userId,
                    "isAdmin" to false
                )
                userRef.set(user).await()
            }
        } catch (e: Exception) {
            Log.e("Firestore", "Error checking/creating user: ${e.localizedMessage}")
        }
    }
}