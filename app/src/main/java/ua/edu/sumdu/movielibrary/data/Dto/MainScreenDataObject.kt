package ua.edu.sumdu.movielibrary.data.Dto

import kotlinx.serialization.Serializable

@Serializable
data class MainScreenDataObject(
    val uid: String = "",
    val email: String = ""
)
