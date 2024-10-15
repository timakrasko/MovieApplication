package ua.edu.sumdu.movielibrary.repository

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import ua.edu.sumdu.movielibrary.data.MainScreenDataObject


class OnlineMovieRepository {
    private val auth: FirebaseAuth = Firebase.auth

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
}