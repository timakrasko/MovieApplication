package ua.edu.sumdu.movielibrary.presentation.models

data class MovieUi(
    val title: String,
    val description: String,
    val imageUrl: String,
    val director: String,
    val genres: List<String> = emptyList()
)