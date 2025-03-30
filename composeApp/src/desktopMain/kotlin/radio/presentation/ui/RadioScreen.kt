package radio.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import radio.data.api.RadioStation
import radio.data.api.Country
import radio.data.api.Language
import radio.data.api.Tag
import radio.presentation.viewmodel.RadioViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RadioScreen(viewModel: RadioViewModel) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCountry by remember { mutableStateOf<String?>(null) }
    var selectedLanguage by remember { mutableStateOf<String?>(null) }
    var selectedTag by remember { mutableStateOf<String?>(null) }
    var countryExpanded by remember { mutableStateOf(false) }
    var languageExpanded by remember { mutableStateOf(false) }
    var tagExpanded by remember { mutableStateOf(false) }

    val stations by viewModel.stations.collectAsState()
    val countries by viewModel.countries.collectAsState()
    val languages by viewModel.languages.collectAsState()
    val tags by viewModel.tags.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadCountries()
        viewModel.loadLanguages()
        viewModel.loadTags()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Search bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { 
                searchQuery = it
                viewModel.searchStations(it)
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search stations...") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Filters
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Country filter
            ExposedDropdownMenuBox(
                expanded = countryExpanded,
                onExpandedChange = { countryExpanded = it },
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    value = selectedCountry ?: "",
                    onValueChange = { },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Select country") }
                )
                ExposedDropdownMenu(
                    expanded = countryExpanded,
                    onDismissRequest = { countryExpanded = false }
                ) {
                    countries.forEach { country ->
                        DropdownMenuItem(
                            onClick = {
                                selectedCountry = country.name
                                viewModel.getStationsByCountry(country.iso_3166_1)
                                countryExpanded = false
                            }
                        ) {
                            Text(country.name)
                        }
                    }
                }
            }

            // Language filter
            ExposedDropdownMenuBox(
                expanded = languageExpanded,
                onExpandedChange = { languageExpanded = it },
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    value = selectedLanguage ?: "",
                    onValueChange = { },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Select language") }
                )
                ExposedDropdownMenu(
                    expanded = languageExpanded,
                    onDismissRequest = { languageExpanded = false }
                ) {
                    languages.forEach { language ->
                        DropdownMenuItem(
                            onClick = {
                                selectedLanguage = language.name
                                viewModel.getStationsByLanguage(language.name)
                                languageExpanded = false
                            }
                        ) {
                            Text(language.name)
                        }
                    }
                }
            }

            // Tag filter
            ExposedDropdownMenuBox(
                expanded = tagExpanded,
                onExpandedChange = { tagExpanded = it },
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    value = selectedTag ?: "",
                    onValueChange = { },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Select tag") }
                )
                ExposedDropdownMenu(
                    expanded = tagExpanded,
                    onDismissRequest = { tagExpanded = false }
                ) {
                    tags.forEach { tag ->
                        DropdownMenuItem(
                            onClick = {
                                selectedTag = tag.name
                                viewModel.getStationsByTag(tag.name)
                                tagExpanded = false
                            }
                        ) {
                            Text(tag.name)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Loading indicator
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        // Error message
        error?.let { errorMessage ->
            Text(
                text = errorMessage,
                color = MaterialTheme.colors.error,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        // Stations list
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(stations) { station ->
                StationCard(station = station)
            }
        }
    }
}

@Composable
fun StationCard(station: RadioStation) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = station.name,
                style = MaterialTheme.typography.h6
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Country: ${station.country ?: "Unknown"}",
                style = MaterialTheme.typography.body1
            )
            Text(
                text = "Language: ${station.language ?: "Unknown"}",
                style = MaterialTheme.typography.body1
            )
            Text(
                text = "Tags: ${station.tags?.joinToString(", ") ?: "No tags"}",
                style = MaterialTheme.typography.body1
            )
        }
    }
} 