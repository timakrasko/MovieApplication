package ua.edu.sumdu.movielibrary.presentation.login

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

import androidx.lifecycle.viewmodel.compose.viewModel
import ua.edu.sumdu.movielibrary.R
import ua.edu.sumdu.movielibrary.data.Dto.MainScreenDataObject
import ua.edu.sumdu.movielibrary.data.Dto.UserProfileScreenDataObject
import ua.edu.sumdu.movielibrary.data.OnlineMovieRepository

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = viewModel(),
    onNavigationToMainScreen: (MainScreenDataObject) -> Unit,
    onNavigateToUserScreen: (UserProfileScreenDataObject) -> Unit
) {
    val loginUiState by loginViewModel.uiState.collectAsState()


    //КАСТЫЛЬ
    val onlineMovieRepository = OnlineMovieRepository()
    val fb = onlineMovieRepository.isSignedIn()
//    if (fb.currentUser != null){
//        onNavigationToMainScreen(MainScreenDataObject(fb.currentUser?.uid?: "", fb.currentUser?.email?: ""))
//    }
    //ДО СИХ ПОР КАСТЫЛЬ

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
            text = loginUiState.email,
            label = "Email",

            ) {
            loginViewModel.setEmail(it)
        }

        Spacer(modifier = Modifier.height(10.dp))

        RoundedCornerTextField(
            text = loginUiState.password,
            label = "Password",
        ) {
            loginViewModel.setPassword(it)
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (loginUiState.error.isNotEmpty()) {
            Text(
                text = loginUiState.error,
                color = Color.Red
            )
        }

        LoginButton(text = "Sign in") {
            loginViewModel.signIn {
                onNavigateToUserScreen(UserProfileScreenDataObject)
            }
        }
        LoginButton(text = "Sign up") {
            loginViewModel.signUp { navData ->
                onNavigationToMainScreen(navData)
            }
        }
    }

}

@Composable
fun LoginButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = {
            onClick()
        },
        modifier = Modifier.fillMaxWidth(0.5f),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.DarkGray,
        )
    ) {
        Text(text = text, color = Color.White)
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
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, Color.Black, RoundedCornerShape(20.dp)),
        label = {
            Text(text = label)
        },
        singleLine = true,
        colors = TextFieldDefaults.colors(
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        ),
    )
}

