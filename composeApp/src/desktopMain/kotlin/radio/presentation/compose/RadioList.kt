package radio.presentation.compose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.koin.java.KoinJavaComponent
import player.presentation.viewmodel.PlayerViewModel
import player.util.RadioState
import radio.presentation.viewmodel.RadioViewModel


@Composable
fun RadioList(
    radioViewModel: RadioViewModel = KoinJavaComponent.getKoin().get(),
    player: PlayerViewModel = KoinJavaComponent.getKoin().get()
) {

    val lazyState = rememberLazyGridState()
    val radioState by radioViewModel.radioState.collectAsState()

    when (radioState) {

        is RadioState.Error -> {
            val error = (radioState as RadioState.Error).data
            Row(
                Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text("Error : $error")
                Spacer(Modifier.size(10.dp))

            }
        }

        is RadioState.Loading -> {

            Row(
                Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                CircularProgressIndicator(Modifier.size(50.dp), MaterialTheme.colors.primary, Dp.Infinity)
                Spacer(Modifier.size(10.dp))
                Text("Loading ...")

            }

        }

        is RadioState.Success -> {

            val data = (radioState as RadioState.Success).data
            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize(),
                state = lazyState,
                contentPadding = PaddingValues(top = 60.dp),
                columns = GridCells.Adaptive(150.dp)
            ) {
                items(data!!) {
                    RadioItemView(it) {
                        player.play(it)
                    }
                }

            }

        }

        else -> {}
    }
}