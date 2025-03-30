package radio.ui.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import radio.data.api.RadioBrowserApi
import radio.data.api.RadioStation
import radio.ui.components.ServerStatusView
import radio.ui.viewmodels.RadioViewModel

@Composable
fun RadioPage(
    viewModel: RadioViewModel,
    modifier: Modifier = Modifier
) {
    var showServerStatus by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxSize()) {
        // Top App Bar
        TopAppBar(
            title = { Text("Radio Browser") },
            actions = {
                IconButton(onClick = { showServerStatus = !showServerStatus }) {
                    Text(if (showServerStatus) "Hide Servers" else "Show Servers")
                }
            }
        )

        // Main Content
        if (showServerStatus) {
            ServerStatusView(
                api = viewModel.api,
                modifier = Modifier.weight(1f)
            )
        } else {
            RadioContent(
                viewModel = viewModel,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun RadioContent(
    viewModel: RadioViewModel,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        // Search Bar
        SearchBar(
            query = viewModel.searchQuery,
            onQueryChange = { viewModel.searchStations(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        // Content
        Box(modifier = Modifier.weight(1f)) {
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
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Error: ${viewModel.error}",
                            color = MaterialTheme.colors.error
                        )
                    }
                }
                else -> {
                    StationList(stations = viewModel.stations)
                }
            }
        }
    }
}

@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier,
        placeholder = { Text("Search radio stations...") },
        singleLine = true,
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") }
    )
}

@Composable
private fun StationList(stations: List<RadioStation>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(stations) { station ->
            StationCard(station)
        }
    }
}

@Composable
private fun StationCard(station: RadioStation) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = station.name,
                style = MaterialTheme.typography.subtitle1
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = station.url,
                style = MaterialTheme.typography.body2
            )
            
            if (station.tags?.isNotEmpty() == true) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Tags: ${station.tags.joinToString(", ")}",
                    style = MaterialTheme.typography.body2
                )
            }
            
            if (station.country != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Country: ${station.country}",
                    style = MaterialTheme.typography.body2
                )
            }
            
            if (station.language != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Language: ${station.language}",
                    style = MaterialTheme.typography.body2
                )
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Votes: ${station.votes}",
                style = MaterialTheme.typography.body2
            )
        }
    }
} 