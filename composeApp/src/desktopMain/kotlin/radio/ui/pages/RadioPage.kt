package radio.ui.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import radio.data.api.RadioBrowserApi
import radio.data.api.RadioStation

import radio.ui.viewmodels.RadioViewModel
import java.io.File

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RadioPage(
    api: RadioBrowserApi,
    viewModel: RadioViewModel
) {
    var showPlayerSelection by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Radio Browser") },
                actions = {
                    IconButton(onClick = { testStreamPlayer() }) {
                        Icon(Icons.Default.PlayArrow, contentDescription = "Test Stream")
                    }
                    TextButton(
                        onClick = { showPlayerSelection = !showPlayerSelection }
                    ) {
                        Text(if (showPlayerSelection) "Hide Player" else "Select Player")
                    }
                }
            )
        }
    ) { padding ->
        if (showPlayerSelection) {
            PlayerSelectionView(
                onPlayerSelected = { playerPath ->
                    viewModel.selectPlayer(playerPath)
                    showPlayerSelection = false
                },
                viewModel = viewModel
            )
        } else {
            RadioContent(
                viewModel = viewModel,
                onStationClick = { station ->
                    viewModel.playStation(station)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun RadioContent(
    viewModel: RadioViewModel,
    onStationClick: (RadioStation) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        SearchBar(
            query = viewModel.searchQuery,
            onQueryChange = viewModel::onSearchQueryChange,
            onSearch = viewModel::searchStations
        )

        Spacer(modifier = Modifier.height(16.dp))

        when {
            viewModel.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            viewModel.error != null -> {
                Text(
                    text = "Error: ${viewModel.error}",
                    color = MaterialTheme.colors.error,
                    modifier = Modifier.padding(16.dp)
                )
            }
            else -> {
                StationList(
                    stations = viewModel.stations,
                    onStationClick = onStationClick
                )
            }
        }
    }
}

@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Search stations...") },
        trailingIcon = {
            IconButton(onClick = onSearch) {
                Icon(Icons.Default.Search, contentDescription = "Search")
            }
        },
        singleLine = true
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun StationList(
    stations: List<RadioStation>,
    onStationClick: (RadioStation) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(stations) { station ->
            StationCard(
                station = station,
                onClick = { onStationClick(station) }
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun StationCard(
    station: RadioStation,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 4.dp,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = station.name,
                style = MaterialTheme.typography.subtitle1
            )
            if (station.url.isNotEmpty()) {
                Text(
                    text = station.url,
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                )
            }
            if (!station.tags.isNullOrEmpty()) {
                Text(
                    text = "Tags: ${station.tags.joinToString(", ")}",
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                )
            }
            if (station.country != null) {
                Text(
                    text = "Country: ${station.country}",
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                )
            }
            if (station.language != null) {
                Text(
                    text = "Language: ${station.language}",
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                )
            }
            Text(
                text = "Votes: ${station.votes}",
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

private fun playStation(station: RadioStation) {
    try {
        val streamPlayerPath = File("StreamPlayer/streamplayer.exe").absolutePath
        val processBuilder = ProcessBuilder(streamPlayerPath, station.url)
        processBuilder.start()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

private fun testStreamPlayer() {
    try {
        val testUrl = "http://mp3channels.webradio.rockantenne.de/heavy-metal"
        val streamPlayerPath = File("StreamPlayer/streamplayer.exe").absolutePath
        println("Streamplayer path: $streamPlayerPath")
        println("Testing URL: $testUrl")
        
        val processBuilder = ProcessBuilder(streamPlayerPath, testUrl)
        val process = processBuilder.start()
        println("Process started with PID: ${process.pid()}")
        
        // Add a listener for process completion
        process.onExit().thenAccept { exitCode ->
            println("Streamplayer process exited with code: $exitCode")
        }
    } catch (e: Exception) {
        println("Error starting streamplayer: ${e.message}")
        println("Stack trace:")
        e.printStackTrace()
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun PlayerSelectionView(
    onPlayerSelected: (String) -> Unit,
    viewModel: RadioViewModel
) {
    var selectedPlayer by remember { mutableStateOf<String?>(null) }
    val players = listOf(
        "StreamPlayer" to "StreamPlayer/streamplayer.exe",
        "VLC" to "C:\\Program Files\\VideoLAN\\VLC\\vlc.exe",
        "Windows Media Player" to "C:\\Program Files\\Windows Media Player\\wmplayer.exe"
    )

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text(
            text = "Select Player",
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Show player status if available
        viewModel.playerStatus?.let { status ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                backgroundColor = if (status.startsWith("Error")) MaterialTheme.colors.error.copy(alpha = 0.1f)
                               else MaterialTheme.colors.primary.copy(alpha = 0.1f)
            ) {
                Text(
                    text = status,
                    modifier = Modifier.padding(16.dp),
                    color = if (status.startsWith("Error")) MaterialTheme.colors.error
                           else MaterialTheme.colors.primary
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(players) { (name, path) ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = 4.dp,
                    onClick = {
                        selectedPlayer = path
                        onPlayerSelected(path)
                    }
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = name,
                                style = MaterialTheme.typography.subtitle1
                            )
                            Text(
                                text = path,
                                style = MaterialTheme.typography.body2,
                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                            )
                        }
                        if (selectedPlayer == path) {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = "Selected",
                                tint = MaterialTheme.colors.primary
                            )
                        }
                    }
                }
            }
        }
    }
} 