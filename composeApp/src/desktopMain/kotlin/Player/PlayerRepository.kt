package Player

interface PlayerRepository {

    fun play(link: String)

    fun shutdown()

}