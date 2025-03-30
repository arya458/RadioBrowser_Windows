package player.presentation.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import player.domain.repository.PlayerRepository
import player.util.PlayerState
import radio.data.api.RadioStation

class PlayerViewModel(
    private val repository: PlayerRepository,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default)
) {
    private val _playerState = MutableStateFlow<PlayerState<String>>(PlayerState.Empty())
    val playerState: StateFlow<PlayerState<String>> = _playerState

    private val _currentStation = MutableStateFlow<RadioStation?>(null)
    val currentStation: StateFlow<RadioStation?> = _currentStation

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun setPlayerDir(playerEXE: String) {
        coroutineScope.launch {
            repository.setPlayerDir(playerEXE).collect { state ->
                _playerState.value = state
            }
        }
    }

    fun checkPlayerDefaultPath() {
        coroutineScope.launch {
            repository.checkPlayerDefaultPath().collect { state ->
                _playerState.value = state
            }
        }
    }

    fun playStation(station: RadioStation) {
        coroutineScope.launch {
            try {
                _error.value = null
                _currentStation.value = station
                repository.play(station.url)
                _isPlaying.value = true
            } catch (e: Exception) {
                _error.value = e.message
                _isPlaying.value = false
            }
        }
    }

    fun stop() {
        coroutineScope.launch {
            try {
                _error.value = null
                repository.stop()
                _isPlaying.value = false
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun pause() {
        coroutineScope.launch {
            try {
                _error.value = null
                repository.pause()
                _isPlaying.value = false
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun resume() {
        coroutineScope.launch {
            try {
                _error.value = null
                repository.resume()
                _isPlaying.value = true
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}