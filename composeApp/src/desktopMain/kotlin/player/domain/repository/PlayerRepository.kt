package player.domain.repository

import kotlinx.coroutines.flow.Flow
import player.util.PlayerState

interface PlayerRepository {

    fun setPlayerDir(playerEXE: String): Flow<PlayerState<String>>

    fun checkPlayerDefaultPath(): Flow<PlayerState<String>>

    suspend fun play(url: String)

    suspend fun stop()

    suspend fun pause()

    suspend fun resume()

    fun shutdown()

}