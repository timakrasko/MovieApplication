package ua.edu.sumdu.movielibrary.domain

data class Movie(
    val title: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val director: String = "",
    val genres: List<String> = listOf()
)