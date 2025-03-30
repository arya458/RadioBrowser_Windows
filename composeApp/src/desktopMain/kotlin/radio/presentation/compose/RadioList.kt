package radio.presentation.compose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import radio.data.api.RadioStation

@Composable
fun RadioList(
    stations: List<RadioStation>,
    isLoading: Boolean,
    error: String?,
    onPlayClick: (RadioStation) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        when {
            isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            error != null -> {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = error,
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.error
                    )
                }
            }
            stations.isEmpty() -> {
                Text(
                    text = "No stations found",
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.body1
                )
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(stations) { station ->
                        RadioItemView(
                            station = station,
                            onClick = { onPlayClick(station) }
                        )
                    }
                }
            }
        }
    }
}