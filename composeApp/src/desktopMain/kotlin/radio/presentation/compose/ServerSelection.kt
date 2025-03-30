package radio.presentation.compose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import radio.data.api.RadioBrowserApi
import radio.data.api.RadioBrowserApi.MirrorInfo

@Composable
fun ServerSelection(
    api: RadioBrowserApi,
    onServerSelected: (String) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var mirrors by remember { mutableStateOf<List<MirrorInfo>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        mirrors = api.getAvailableMirrors()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Available Servers: ${mirrors.count { it.isAvailable }}/${mirrors.size}",
                style = MaterialTheme.typography.body2
            )
            Button(
                onClick = {
                    showDialog = true
                    isLoading = true
                }
            ) {
                Text("Select Server")
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Select Server") },
                text = {
                    if (isLoading) {
                        CircularProgressIndicator()
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 400.dp)
                        ) {
                            items(mirrors) { mirror ->
                                ServerItem(
                                    mirror = mirror,
                                    onSelect = {
                                        onServerSelected(mirror.url)
                                        showDialog = false
                                    }
                                )
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Close")
                    }
                }
            )
        }
    }
}

@Composable
private fun ServerItem(
    mirror: MirrorInfo,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        elevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = mirror.url,
                    style = MaterialTheme.typography.body1,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (mirror.isAvailable) {
                        "Response time: ${mirror.responseTime}ms"
                    } else {
                        "Unavailable"
                    },
                    style = MaterialTheme.typography.body2,
                    color = if (mirror.isAvailable) MaterialTheme.colors.onSurface else MaterialTheme.colors.error
                )
            }
            if (mirror.isAvailable) {
                Button(onClick = onSelect) {
                    Text("Select")
                }
            }
        }
    }
} 