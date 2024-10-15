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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

import androidx.lifecycle.viewmodel.compose.viewModel
import ua.edu.sumdu.movielibrary.R
import ua.edu.sumdu.movielibrary.data.MainScreenDataObject
import ua.edu.sumdu.movielibrary.repository.OnlineMovieRepository

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(factory = LoginViewModel.Factory),
    onNavigationToMainScreen: (MainScreenDataObject) -> Unit
) {
    val repository = OnlineMovieRepository()


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
            text = viewModel.state.email,
            label = "Email",

            ) {
            viewModel.setEmail(it)
        }

        Spacer(modifier = Modifier.height(10.dp))

        RoundedCornerTextField(
            text = viewModel.state.password,
            label = "Password",
        ) {
            viewModel.setPassword(it)
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (viewModel.state.error.isNotEmpty()) {
            Text(
                text = viewModel.state.error,
                color = Color.Red
            )
        }

        LoginButton(text = "Sign in") {
           viewModel.signIn { navData ->
               onNavigationToMainScreen(navData)
           }
        }
        LoginButton(text = "Sign up") {
            viewModel.signUp { navData ->
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

