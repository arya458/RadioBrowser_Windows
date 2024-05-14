import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    val icon = painterResource("drawable/icon.png")
    Window(
        onCloseRequest = ::exitApplication,
        icon = icon,
        title = "RadioBrowser",
    ) {
        App()
    }
}