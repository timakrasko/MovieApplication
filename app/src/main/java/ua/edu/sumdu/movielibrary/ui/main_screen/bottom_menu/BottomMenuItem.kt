package ua.edu.sumdu.movielibrary.ui.main_screen.bottom_menu

import ua.edu.sumdu.movielibrary.R

sealed class BottomMenuItem(
    val route: String,
    val title: String,
    val iconId: Int
) {
    object Home: BottomMenuItem(
        route = "",
        title = "Home",
        iconId = R.drawable.ic_home
    )
    object Settings: BottomMenuItem(
        route = "",
        title = "Settings",
        iconId = R.drawable.ic_settings
    )
    object Favorite: BottomMenuItem(
        route = "",
        title = "Favorite",
        iconId = R.drawable.ic_favs
    )
}