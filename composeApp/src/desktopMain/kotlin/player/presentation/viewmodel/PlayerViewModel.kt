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
import java.io.File

class PlayerViewModel(private val player: PlayerRepository = KoinJavaComponent.getKoin().get()) {

    private var lastProcess: Process? = null
    private var playerScope: Job? = null
    private val scope = CoroutineScope(Dispatchers.IO)

    private val _playerState: MutableStateFlow<PlayerState<String>> = MutableStateFlow(PlayerState.Empty())
    val playerState: StateFlow<PlayerState<String>> = _playerState

    private val _playerReadyState: MutableStateFlow<PlayerReadyState<Station>> = MutableStateFlow(PlayerReadyState.Waiting())
    val playerReadyState: StateFlow<PlayerReadyState<Station>> = _playerReadyState

    init {
        checkPlayerDefaultPath()
    }

    fun setPlayerDir(playerEXE: String) {
        scope.launch {
            player.setPlayerDir(playerEXE).collectLatest {
                _playerState.value = it
            }
        }
    }

    private fun checkPlayerDefaultPath(){
        scope.launch {
            player.checkPlayerDefaultPath().collectLatest {
                _playerState.value = it
            }
        }
    }

    fun play(station : Station) {
        if (playerScope != null) {
            lastProcess?.destroyForcibly()?.destroy()
        }
        playerScope = scope.launch {

            flow<PlayerReadyState<Station>> {
                emit(PlayerReadyState.Loading())
                try {
                    lastProcess = player.play(station.url)
                    emit(PlayerReadyState.Playing(station))
                }catch (e:Exception){
                    println(e.message)
                    emit(PlayerReadyState.Error(e.message.toString()))
                }
            }.collectLatest {
                _playerReadyState.value = it
            }
        }
    }

    fun shutdown() {
        player.shutdown()
        lastProcess?.destroyForcibly()?.destroy()
        playerScope?.cancel()
        scope.cancel()
    }

}