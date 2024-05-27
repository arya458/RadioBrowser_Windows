package player.presentation.viewmodel

import de.sfuhrm.radiobrowser4j.Station
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import org.koin.java.KoinJavaComponent
import player.domain.repository.PlayerRepository
import player.util.PlayerReadyState
import player.util.PlayerState

class SharedViewModel(private val player: PlayerRepository = KoinJavaComponent.getKoin().get()) {



}