package ua.edu.sumdu.movielibrary.data.Dto

import kotlinx.serialization.Serializable
import ua.edu.sumdu.movielibrary.domain.Movie

@Serializable
data class MovieDto(
    val title: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val director: String = "",
    val genres: List<String> = listOf()
)

fun Movie.toMovieDto(): MovieDto{
    return MovieDto(
        title = title,
        description = description,
        imageUrl = imageUrl,
        director = director,
        genres = genres
    )
}