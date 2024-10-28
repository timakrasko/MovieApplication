package ua.edu.sumdu.movielibrary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import ua.edu.sumdu.movielibrary.ui.navigation.MovieNavHost
import ua.edu.sumdu.movielibrary.ui.navigation.MovieNavHostt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            MovieNavHostt(navController = navController)
        }
    }
}