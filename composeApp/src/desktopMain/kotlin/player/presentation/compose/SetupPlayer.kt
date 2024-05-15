package player.presentation.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import org.koin.java.KoinJavaComponent
import player.domain.repository.PlayerRepository
import player.presentation.viewmodel.PlayerViewModel
import java.io.File

@Composable
fun SetupPlayer(viewModel: PlayerViewModel = KoinJavaComponent.getKoin().get()){

    val showPicker = remember { mutableStateOf(false) }
    FilePicker(
        show = showPicker.value, fileExtensions = listOf("exe"), title = "mpc-hc64"
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

        Text("Please Download And Select K-Lite Codec exe")
        Button({
            showPicker.value = true
        }) {
            Text("Select")
        }

    }

}