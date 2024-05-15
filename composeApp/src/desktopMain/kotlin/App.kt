import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import de.sfuhrm.radiobrowser4j.Station
import org.koin.java.KoinJavaComponent
import org.koin.java.KoinJavaComponent.getKoin
import player.domain.repository.PlayerRepository
import player.presentation.compose.SetupPlayer
import player.presentation.viewmodel.PlayerViewModel
import player.util.PlayerState
import radio.data.repository.RadioImpl
import java.io.File

@Composable
fun App(player: PlayerViewModel = getKoin().get()) {
    MaterialTheme {



        val lazyState = rememberLazyGridState()
        val playerState  by player.playerState.collectAsState()



        Surface(Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
            when (playerState) {

                is PlayerState.Empty -> {

                    SetupPlayer()

                }

                else -> {

                    val radioList : MutableList<Station> = mutableStateListOf()
                    RadioImpl(limit = 300).getListTopClickStations{
                        if (!radioList.contains(it)) {
                            radioList.add(it)
                        }
                    }

                    LazyVerticalGrid(
                        modifier = Modifier.padding(10.dp).fillMaxSize(),
                        state = lazyState,
                        columns = GridCells.Fixed(5)
                    ) {
                        items(radioList) {
                            Card(modifier = Modifier.padding(10.dp).size(100.dp).clickable {
                                player.play(it)
                            }, backgroundColor = MaterialTheme.colors.surface, elevation = 10.dp) {

                                Column(
                                    Modifier.fillMaxSize().padding(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {

                                    Text(it.name)
                                    Spacer(Modifier.size(10.dp))
                                    Text(it.state)
                                }

                            }

                        }
                    }

                }
            }
        }


    }
}
