package ua.edu.sumdu.movielibrary.data

data class Movie(
    val title: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val genres: List<String> = listOf()
)