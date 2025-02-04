package ua.edu.sumdu.movielibrary.data

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import ua.edu.sumdu.movielibrary.data.Dto.MainScreenDataObject

class OnlineMovieRepository {
    private val auth: FirebaseAuth = Firebase.auth

    fun isSignedIn(): FirebaseAuth{
        return auth
    }

    fun signUp(
        email: String?,
        password: String?,
        onSignUpSuccess: (MainScreenDataObject) -> Unit,
        onSignUpFailure: (String) -> Unit
    ) {
        if (email.isNullOrBlank() || password.isNullOrBlank()) {
            onSignUpFailure("Email or password can't be empty")
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result.user?.let { user ->
                        createUserInFirestoreIfNotExists(user.uid, user.email!!)
                        onSignUpSuccess(MainScreenDataObject(user.uid, user.email!!))
                    }
                }
            }
            .addOnFailureListener {
                onSignUpFailure(it.message ?: "Sign Up Error")
            }
    }

    fun signIn(
        email: String?,
        password: String?,
        onSignInSuccess: (MainScreenDataObject) -> Unit,
        onSignInFailure: (String) -> Unit
    ) {
        if (email.isNullOrBlank() || password.isNullOrBlank()) {
            onSignInFailure("Email or password can't be empty")
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result.user?.let { user ->
                        onSignInSuccess(MainScreenDataObject(user.uid, user.email!!))
                    }
                }
            }
            .addOnFailureListener {
                onSignInFailure(it.message ?: "Sign In Error")
            }
    }

    fun createUserInFirestoreIfNotExists(userId: String, email: String) {
        val userRef = Firebase.firestore.collection("users").document(userId)

        userRef.get().addOnSuccessListener { document ->
            if (!document.exists()) {
                val user = hashMapOf(
                    "email" to email,
                    "createdAt" to System.currentTimeMillis()
                )
                userRef.set(user)
            }
        }
    }
}