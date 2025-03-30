package radio.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import radio.data.api.RadioBrowserApi
import radio.data.api.RadioStation
import java.io.File

class RadioViewModel {
    private val scope = CoroutineScope(Dispatchers.Default)
    private val api = RadioBrowserApi()

    var stations by mutableStateOf<List<RadioStation>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    var searchQuery by mutableStateOf("")
        private set

    var selectedPlayer by mutableStateOf<String?>(null)
        private set

    var playerStatus by mutableStateOf<String?>(null)
        private set

    fun onSearchQueryChange(query: String) {
        searchQuery = query
    }

    fun searchStations() {
        if (searchQuery.isBlank()) return

        scope.launch {
            try {
                isLoading = true
                error = null
                stations = api.searchStations(searchQuery)
            } catch (e: Exception) {
                error = e.message
                stations = emptyList()
            } finally {
                isLoading = false
            }
        }
    }

    fun refreshMirrors() {
        scope.launch {
            try {
                api.refreshMirrors()
            } catch (e: Exception) {
                error = e.message
            }
        }
    }

    fun selectPlayer(playerPath: String) {
        selectedPlayer = playerPath
        checkPlayerDependencies(playerPath)
    }

    private fun checkPlayerDependencies(playerPath: String) {
        when {
            playerPath.contains("streamplayer.exe") -> {
                val streamPlayerDir = File(playerPath).parentFile
                val ffplayFile = File(streamPlayerDir, "ffplay.exe")
                if (!ffplayFile.exists()) {
                    playerStatus = "Error: ffplay.exe not found in ${streamPlayerDir.absolutePath}. Please copy ffplay.exe from FFmpeg to this directory."
                } else {
                    playerStatus = "Ready: All dependencies found"
                }
            }
            playerPath.contains("vlc.exe") -> {
                val vlcFile = File(playerPath)
                if (!vlcFile.exists()) {
                    playerStatus = "Error: VLC not found at ${vlcFile.absolutePath}. Please install VLC or select a different player."
                } else {
                    playerStatus = "Ready: VLC found"
                }
            }
            playerPath.contains("wmplayer.exe") -> {
                val wmpFile = File(playerPath)
                if (!wmpFile.exists()) {
                    playerStatus = "Error: Windows Media Player not found at ${wmpFile.absolutePath}. Please install Windows Media Player or select a different player."
                } else {
                    playerStatus = "Ready: Windows Media Player found"
                }
            }
            else -> {
                playerStatus = "Error: Unknown player type"
            }
        }
    }

    fun playStation(station: RadioStation) {
        try {
            val playerPath = selectedPlayer ?: "StreamPlayer/streamplayer.exe"
            val playerFile = File(playerPath)
            
            if (!playerFile.exists()) {
                error = "Player not found at: $playerPath"
                return
            }

            val processBuilder = ProcessBuilder(playerPath, station.url)
            processBuilder.directory(playerFile.parentFile)
            val process = processBuilder.start()
            
            process.onExit().thenAccept { exitCode ->
                if (exitCode.exitValue() != 0) {
                    error = "Player exited with code: $exitCode"
                }
            }
        } catch (e: Exception) {
            error = "Failed to start player: ${e.message}"
        }
    }
} 