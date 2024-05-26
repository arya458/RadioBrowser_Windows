package player.domain.repository

import kotlinx.coroutines.flow.Flow
import player.util.PlayerState

interface PlayerRepository {

    fun setPlayerDir(playerEXE: String): Flow<PlayerState<String>>

    fun checkPlayerDefaultPath(): Flow<PlayerState<String>>

    fun play(link: String): Process?

    fun shutdown()

}