package player.data.repository

import player.domain.repository.PlayerRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import player.util.PlayerState
import java.io.File

class PlayerImpl: PlayerRepository {
    private var runtime: Runtime = Runtime.getRuntime()
    private var playerDir: File? =null

    init {
        println("PlayerImpl Created")
    }
    override fun setPlayerDir(playerEXE: String): Flow<PlayerState<String>> {
        return flow<PlayerState<String>> {

            emit(PlayerState.Empty())
            try {
                playerDir = File(playerEXE)
                emit(PlayerState.Ready(playerEXE))
            }catch (e:Exception){
                emit(PlayerState.Error(e.message.toString()))
            }
        }
    }

    override fun checkPlayerDefaultPath(): Flow<PlayerState<String>> {
        val defPath = "C:\\Program Files (x86)\\K-Lite Codec Pack\\MPC-HC64\\mpc-hc64.exe"

        return flow<PlayerState<String>> {
            emit(PlayerState.Empty())
            try {
                val def = File(defPath)
                if (def.exists() && def.isFile){
                    playerDir = File(defPath)
                    emit(PlayerState.Ready(defPath))
                }
            } catch (e: Exception) {
                println(e.message)
            }
        }
    }


    override fun play(link: String): Process? {
        return runtime.exec("${playerDir?.path} $link")
    }



    override fun shutdown() {
        println("Player.Player Cancelled")
        runtime.addShutdownHook(Thread("Cancelled"))
    }


}