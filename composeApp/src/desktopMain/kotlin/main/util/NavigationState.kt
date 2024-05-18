package main.util


sealed class NavigationState<T>(
    val view: T? = null) {
    class Redio<T>(view: T?) : NavigationState<T>(view = view)

    class Search<T>(view: T?) : NavigationState<T>(view = view)

    class Favorite<T>(view: T?) : NavigationState<T>(view = view)
}


