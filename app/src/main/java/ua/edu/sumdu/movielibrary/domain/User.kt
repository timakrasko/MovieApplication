package ua.edu.sumdu.movielibrary.domain

data class User(
    val uid: String = "",
    val email: String = "",
    val username: String = "",
    val watchedList: List<String> = listOf(),
    val createdAt: Long = 0,
    val imageUrl: String? = null
)
