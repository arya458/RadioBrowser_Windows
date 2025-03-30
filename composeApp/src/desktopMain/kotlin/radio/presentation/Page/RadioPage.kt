package radio.presentation.Page

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.koin.compose.koinInject
import radio.presentation.compose.RadioList
import radio.presentation.compose.Search.SearchView
import radio.presentation.compose.ServerSelection
import radio.presentation.viewmodel.RadioViewModel
import player.presentation.viewmodel.PlayerViewModel
import radio.data.api.RadioBrowserApi

@Composable
fun RadioPage(
    viewModel: RadioViewModel = koinInject(),
    playerViewModel: PlayerViewModel = koinInject()
) {
    val api: RadioBrowserApi = koinInject()
    var searchQuery by remember { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }

    LaunchedEffect(searchQuery) {
        if (searchQuery.isNotBlank()) {
            isSearching = true
            viewModel.searchStations(searchQuery)
        } else {
            viewModel.searchStations("")
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
//        // Server Selection
//        ServerSelection(
//            api = api,
//            onServerSelected = {
//                viewModel.refreshMirrors()
//            }
//        )

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search stations...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            singleLine = true
        )

        // Loading indicator
        if (viewModel.isLoading.collectAsState().value) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        // Error message
        viewModel.error.collectAsState().value?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colors.error,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        // Radio List
        RadioList(
            stations = viewModel.stations.collectAsState().value,
            isLoading = viewModel.isLoading.collectAsState().value,
            error = viewModel.error.collectAsState().value,
            onPlayClick = { station ->
                playerViewModel.playStation(station)
            }
        )
    }
}