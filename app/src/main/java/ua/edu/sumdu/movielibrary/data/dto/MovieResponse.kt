package ua.edu.sumdu.movielibrary.data.dto

import com.google.gson.annotations.SerializedName

data class MovieResponse (
    @SerializedName("Title")
    val title: String? = null,
    @SerializedName("Year")
    val releaseYear: String? = null,
    @SerializedName("Plot")
    val description: String? = null,
    @SerializedName("Poster")
    val imageUrl: String? = null,
    @SerializedName("Director")
    val director: String? = null,
    @SerializedName("Genre")
    val genres: String? = null,
//    val rating: Int? = null
)