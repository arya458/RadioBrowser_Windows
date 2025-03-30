package player.presentation.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import org.koin.java.KoinJavaComponent
import player.domain.repository.PlayerRepository
import player.presentation.viewmodel.PlayerViewModel
import player.util.PlayerState
import java.io.File

@Composable
fun SetupPlayer(viewModel: PlayerViewModel = KoinJavaComponent.getKoin().get()) {
    val playerState = viewModel.playerState.collectAsState()
    val showPicker = remember { mutableStateOf(false) }

    FilePicker(
        show = showPicker.value,
        fileExtensions = listOf("exe"),
        title = "Select StreamPlayer.exe"
    ) { platformFile ->
        if (platformFile != null) {
            viewModel.setPlayerDir(platformFile.path)
        }
        showPicker.value = false
    }

    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (val state = playerState.value) {
            is PlayerState.Loading -> {
                CircularProgressIndicator()
            }
            is PlayerState.Error -> {
                Text(state.errorMsg ?: "Unknown error")
                Button(onClick = { showPicker.value = true }) {
                    Text("Select Player")
                }
            }
            is PlayerState.Empty -> {
                Text("Please select StreamPlayer.exe")
                Button(onClick = { showPicker.value = true }) {
                    Text("Select Player")
                }
            }
            else -> {
                // Do nothing, as we'll be showing the radio page
            }
        }
    }
}