package radio.util

sealed class RadioState<T>(
    val data: T? = null,
    val errorMsg: String? = null
) {
    class Success<T>(data: T?) : RadioState<T>(data = data)

    class Error<T>(msg: String) : RadioState<T>(errorMsg = msg)

    class Loading<T> : RadioState<T>()
}