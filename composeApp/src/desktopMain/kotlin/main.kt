import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import main.presentation.App
import org.koin.core.context.startKoin
import player.di.PlayerModule
import radio.di.RadioModule


fun main() = application {
    val icon = painterResource("drawable/icon.png")


//    StreamPlayer()
    startKoin {
        modules(RadioModule)
        modules(PlayerModule)
    }
    Window(
        onCloseRequest = ::exitApplication,
        icon = icon,
        title = "RadioBrowser",
    ) {
        App()
    }
}