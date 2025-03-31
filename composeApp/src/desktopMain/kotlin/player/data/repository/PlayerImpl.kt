package player.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import player.domain.repository.PlayerRepository
import player.util.PlayerState
import java.io.File
import java.io.IOException

class PlayerImpl : PlayerRepository {
    private var process: Process? = null
    private var playerPath = "C:\\Program Files (x86)\\K-Lite Codec Pack\\MPC-HC64\\mpc-hc64.exe"

    override fun setPlayerDir(playerEXE: String): Flow<PlayerState<String>> = flow {
        emit(PlayerState.Loading<String>())
        try {
            playerPath = playerEXE
            val file = File(playerPath)
            if (file.exists() && file.canExecute()) {
                emit(PlayerState.Success(playerPath))
            } else {
                emit(PlayerState.Error("Player executable not found or not executable"))
            }
        } catch (e: Exception) {
            emit(PlayerState.Error(e.message ?: "Unknown error"))
        }
    }

    override fun checkPlayerDefaultPath(): Flow<PlayerState<String>> = flow {
        emit(PlayerState.Loading<String>())
        try {
            val file = File(playerPath)
            if (file.exists() && file.canExecute()) {
                emit(PlayerState.Success(playerPath))
            } else {
                emit(PlayerState.Error("Default player executable not found"))
            }
        } catch (e: Exception) {
            emit(PlayerState.Error(e.message ?: "Unknown error"))
        }
    }

    override suspend fun play(url: String) {
        stop()
        try {
            val command = listOf(playerPath, "/play ", url)
            process = withContext(Dispatchers.IO) {
                ProcessBuilder(command)
                    .directory(File(System.getProperty("user.dir")))
                    .start()
            }
        } catch (e: IOException) {
            throw RuntimeException("Failed to start player: ${e.message}")
        }
    }

    override suspend fun stop() {
        process?.destroyForcibly()?.waitFor()
        process = null
    }

    override suspend fun pause() {
        process?.destroyForcibly()?.waitFor()
        process = null
    }

    override suspend fun resume() {
        process?.let { p ->
            if (!p.isAlive) {
                throw RuntimeException("Player process is not running")
            }
        } ?: throw RuntimeException("No active player process")
    }

    override fun shutdown() {
        process?.destroyForcibly()?.waitFor()
        process = null
    }
}