package main.util

sealed class AppScreens(val route: String) {

    object Main : AppScreens("Main") {
        object Loading : AppScreens("Loading")
        object Home : AppScreens("Home")
        object Top : AppScreens("Top")
        object Favourite : AppScreens("Favourite")
    }
}
