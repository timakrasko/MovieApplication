package ua.edu.sumdu.movielibrary.data.dto

import kotlinx.serialization.Serializable
import ua.edu.sumdu.movielibrary.domain.User

@Serializable
data class UserDto(
    val uid: String = "",
    val email: String = "",
    val username: String = "",
    val watchedList: List<String> = listOf(),
    val createdAt: String = "",
    val imageUrl: String = "",
    val isAdmin: Boolean = false
)

fun User.toUserDto(): UserDto{
    return UserDto(
        uid = uid,
        email = email,
        username = username,
        watchedList = watchedList,
        createdAt = createdAt,
        imageUrl = imageUrl,
        isAdmin = isAdmin
    )
}