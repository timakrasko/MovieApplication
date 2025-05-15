package ua.edu.sumdu.movielibrary.domain

data class User(
    val uid: String = "",
    val email: String = "",
    val username: String = "",
    val watchedList: List<String> = listOf(),
    val planedList: List<String> = listOf(),
    val friendList: List<String> = listOf(),
    val createdAt: String = "",
    val imageUrl: String = "",
    val isAdmin: Boolean = false
)
