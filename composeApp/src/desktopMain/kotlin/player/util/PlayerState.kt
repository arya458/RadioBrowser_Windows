package player.util

sealed class PlayerState<T>(
    val data: T? = null,
    val errorMsg: String? = null
) {
    class Ready<T>(data: T?) : PlayerState<T>(data = data)
    class Loading<T> : PlayerState<T>()
    class Success<T>(data: T?) : PlayerState<T>(data = data)
    class Error<T>(msg: String) : PlayerState<T>(errorMsg = msg)
    class Empty<T> : PlayerState<T>()
}

sealed class PlayerReadyState<T>(
    val data: T? = null,
    val errorMsg: String? = null
) {
    class Playing<T>(data: T?) : PlayerReadyState<T>(data = data)
    class Loading<T> : PlayerReadyState<T>()
    class Error<T>(msg: String) : PlayerReadyState<T>(errorMsg = msg)
    class Waiting<T> : PlayerReadyState<T>()
}