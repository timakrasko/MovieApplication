package ua.edu.sumdu.movielibrary.data.dto

import kotlinx.serialization.Serializable
import ua.edu.sumdu.movielibrary.domain.Movie

@Serializable
data class MovieDto(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val director: String = "",
    val releaseYear: String = "",
    val genres: List<String> = listOf(),
    val rating: Int? = null
)

fun Movie.toMovieDto(): MovieDto{
    return MovieDto(
        id = id,
        title = title,
        description = description,
        imageUrl = imageUrl,
        director = director,
        releaseYear = releaseYear,
        genres = genres,
        rating = rating
    )
}