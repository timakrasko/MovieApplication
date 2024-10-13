package ua.edu.sumdu.movielibrary.ui.login

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import ua.edu.sumdu.movielibrary.R
import ua.edu.sumdu.movielibrary.data.MainScreenDataObject

@Composable
fun LoginScreen(
    onNavigationToMainScreen: (MainScreenDataObject) -> Unit
) {

    val auth = remember {
        Firebase.auth
    }

    val errorState = remember {
        mutableStateOf("")
    }

    val emailState = remember {
        mutableStateOf("")
    }
    val passwordState = remember {
        mutableStateOf("")
    }

    Image(
        painter = painterResource(id = R.drawable.lotr),
        contentDescription = "BG",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = 50.dp, end = 50.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        RoundedCornerTextField(
            text = emailState.value,
            label = "Email",

            ) {
            emailState.value = it
        }

        Spacer(modifier = Modifier.height(10.dp))

        RoundedCornerTextField(
            text = passwordState.value,
            label = "Password",

            ) {
            passwordState.value = it
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (errorState.value.isNotEmpty()) {
            Text(
                text = errorState.value,
                color = Color.Red
            )
        }

        LoginButton(text = "Sign in") {
            signIn(
                auth,
                emailState.value,
                passwordState.value,
                onSignInSuccess = { navData ->
                    onNavigationToMainScreen(navData)
                    Log.d("MyLog", "Sign In Success")
                },
                onSignInFailure = { error ->
                    errorState.value = error
                }
            )
        }
        LoginButton(text = "Sign up") {
            signUp(
                auth,
                emailState.value,
                passwordState.value,
                onSignUpSuccess = { navData ->
                    onNavigationToMainScreen(navData)
                    Log.d("MyLog", "Sign Up Success")
                },
                onSignUpFailure = { error ->
                    errorState.value = error
                }
            )
        }
    }

}

@Composable
fun LoginButton(
    text: String,
    onClick: () -> Unit
) {
    Button(onClick = {
        onClick()
    },
        modifier = Modifier.fillMaxWidth(0.5f),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.LightGray,
        )
    ) {
        Text(text = text, color = Color.Black)
    }
}

@Composable
fun RoundedCornerTextField(
    text: String,
    label: String,
    onValueChange: (String) -> Unit
) {
    TextField(
        value = text,
        onValueChange = {
            onValueChange(it)
        },
        shape = RoundedCornerShape(20.dp),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        ),
        modifier = Modifier.fillMaxWidth().border(2.dp, Color.Black, RoundedCornerShape(20.dp)),
        label = {
            Text(text = label)
        },
        singleLine = true
    )
}


fun signUp(
    auth: FirebaseAuth,
    email: String,
    password: String,
    onSignUpSuccess: (MainScreenDataObject) -> Unit,
    onSignUpFailure: (String) -> Unit
) {
    if (email.isBlank() || password.isBlank()) {
        onSignUpFailure("Email or password can't be empty")
        return
    }

    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSignUpSuccess(
                    MainScreenDataObject(
                        task.result.user?.uid!!,
                        task.result.user?.email!!
                    )
                )
            }
        }
        .addOnFailureListener {
            onSignUpFailure(it.message ?: "Sign Up Error")
        }
}

private fun signIn(
    auth: FirebaseAuth,
    email: String,
    password: String,
    onSignInSuccess: (MainScreenDataObject) -> Unit,
    onSignInFailure: (String) -> Unit
) {
    if (email.isBlank() || password.isBlank()) {
        onSignInFailure("Email or password can't be empty")
        return
    }

    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSignInSuccess(
                    MainScreenDataObject(
                        task.result.user?.uid!!,
                        task.result.user?.email!!
                    )
                )
            }
        }
        .addOnFailureListener {
            onSignInFailure(it.message ?: "Sign Up Error")
        }
}
