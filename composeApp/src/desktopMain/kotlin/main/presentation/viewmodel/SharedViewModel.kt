package player.presentation.viewmodel

import org.koin.java.KoinJavaComponent
import player.domain.repository.PlayerRepository

class SharedViewModel(private val player: PlayerRepository = KoinJavaComponent.getKoin().get()) {



}