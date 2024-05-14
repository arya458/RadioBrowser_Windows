package Player

import kotlinx.coroutines.*
import java.io.File

class PlayerImpl constructor(val playerDir: File) : PlayerRepository {
    private val scope = CoroutineScope(Dispatchers.IO)
    private var runtime: Runtime = Runtime.getRuntime()
    private var lastProcess: Process? = null
    private var playerScope: Job? = null

    override fun play(link: String) {
        if (playerScope!=null){
            lastProcess?.destroyForcibly()?.destroy()
        }
        playerScope = scope.launch {
            lastProcess = runtime.exec("${playerDir.path} $link")
        }
    }

    override fun shutdown() {
        println("Player.Player Cancelled")
        runtime.addShutdownHook(Thread("Cancelled"))
    }


}