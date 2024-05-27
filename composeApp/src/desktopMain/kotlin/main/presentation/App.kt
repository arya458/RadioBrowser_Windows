package main.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import org.koin.java.KoinJavaComponent.getKoin
import player.presentation.compose.SetupPlayer
import player.presentation.viewmodel.PlayerViewModel
import player.util.PlayerState
import radio.presentation.Page.RadioPage

@Composable
fun App(player: PlayerViewModel = getKoin().get()) {
    MaterialTheme() {




        val playerState  by player.playerState.collectAsState()



        Surface(Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
            when (playerState) {

                is PlayerState.Empty -> {

                    SetupPlayer()

                }

                else -> {
                    RadioPage()

                }
            }
        }


    }
}
