import Player.PlayerImpl
import Radio.RadioImpl
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
import de.sfuhrm.radiobrowser4j.*
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.ui.tooling.preview.Preview

import java.io.File

@OptIn(ExperimentalResourceApi::class)
@Composable
@Preview
fun App() {
    MaterialTheme {

        val playerDir: MutableState<File?> = remember { mutableStateOf(null) }
        val showPicker = remember { mutableStateOf(false) }
        val lazyState = rememberLazyGridState()

        FilePicker(
            show = showPicker.value, fileExtensions = listOf("exe"), title = "mpc-hc64"
        ) { platformFile ->
            if (platformFile != null) {
                playerDir.value = File(platformFile.path)
            }
            showPicker.value = false
        }
        Surface(Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
            when (playerDir.value) {
                null -> {
                    Column(
                        Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        Text("Please Download And Select K-Lite Codec exe")
                        Button({
                            showPicker.value = true
                        }) {
                            Text("Select")
                        }

                    }
                }

                else -> {

                    val radioList : MutableList<Station> = mutableStateListOf()
                    RadioImpl(limit = 300).getListTopClickStations{
                        if (!radioList.contains(it)) {
                            radioList.add(it)
                        }
                    }
                    val player = PlayerImpl(playerDir.value!!)


                    LazyVerticalGrid(
                        modifier = Modifier.padding(10.dp).fillMaxSize(),
                        state = lazyState,
                        columns = GridCells.Fixed(5)
                    ) {
                        items(radioList) {
                            Card(modifier = Modifier.padding(10.dp).size(100.dp).clickable {
                                player.play(it.url)
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
