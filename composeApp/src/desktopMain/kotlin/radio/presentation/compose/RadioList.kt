package radio.presentation.compose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import radio.data.api.RadioStation

@Composable
fun RadioList(
    stations: List<RadioStation>,
    isLoading: Boolean,
    error: String?,
    onPlayClick: (RadioStation) -> Unit
) {
    Column(Modifier.fillMaxSize(), Arrangement.Center,Alignment.CenterHorizontally) {
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
                    LazyVerticalGrid(
                        columns = GridCells.FixedSize(200.dp),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(8.dp),
//                    verticalArrangement = Arrangement.spacedBy(8.dp)
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
        Spacer(Modifier.height(16.dp))
        Text("Created with ❤\uFE0F by Aria Danesh",Modifier.wrapContentSize(),textAlign = TextAlign.Center, style = MaterialTheme.typography.body1,color = MaterialTheme.colors.onBackground)
        Spacer(Modifier.height(16.dp))
    }
}