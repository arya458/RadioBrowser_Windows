package main

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.runtime.Composable
import di.appModule
import org.koin.core.context.startKoin
import org.koin.compose.koinInject
import player.di.PlayerModule
import main.presentation.App

fun main() = application {
    startKoin {
        modules(appModule, PlayerModule)
    }

    Window(
        onCloseRequest = ::exitApplication,
        title = "Radio Browser"
    ) {
        MainContent()
    }
}

@Composable
private fun MainContent() {
    App()
}