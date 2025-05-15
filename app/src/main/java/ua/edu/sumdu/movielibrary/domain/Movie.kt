package ua.edu.sumdu.movielibrary.domain

data class Movie(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val director: String = "",
    val releaseYear: String = "",
    val genres: List<String> = listOf(),
    val rating: Int? = null
)