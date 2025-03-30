package radio.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import radio.data.api.RadioBrowserApi
import radio.data.api.RadioBrowserApi.ServerStatus
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ServerStatusView(
    api: RadioBrowserApi,
    modifier: Modifier = Modifier
) {
    var serverStatuses by remember { mutableStateOf<List<ServerStatus>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            isLoading = true
            serverStatuses = api.getServerStatuses()
        } catch (e: Exception) {
            error = e.message
        } finally {
            isLoading = false
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Server Status",
                style = MaterialTheme.typography.h6
            )
            Button(
                onClick = {
                    // Refresh server statuses
                    serverStatuses = api.getServerStatuses()
                }
            ) {
                Text("Refresh")
            }
        }

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (error != null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Error: $error",
                    color = MaterialTheme.colors.error
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(serverStatuses) { status ->
                    ServerStatusCard(status)
                }
            }
        }
    }
}

@Composable
private fun ServerStatusCard(status: ServerStatus) {
    val dateFormat = remember { SimpleDateFormat("HH:mm:ss", Locale.getDefault()) }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = status.ip,
                    style = MaterialTheme.typography.subtitle1
                )
                ServerStatusIndicator(status.isAvailable)
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (status.name != null) {
                Text(
                    text = "Name: ${status.name}",
                    style = MaterialTheme.typography.body2
                )
            }

            Text(
                text = "Response Time: ${status.responseTime}ms",
                style = MaterialTheme.typography.body2
            )

            if (status.error != null) {
                Text(
                    text = "Error: ${status.error}",
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.error
                )
            }

            Text(
                text = "Last Checked: ${dateFormat.format(Date(status.lastChecked))}",
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
private fun ServerStatusIndicator(isAvailable: Boolean) {
    val color = if (isAvailable) {
        MaterialTheme.colors.primary
    } else {
        MaterialTheme.colors.error
    }
    
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier.size(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier.size(8.dp),
                shape = MaterialTheme.shapes.small,
                color = color
            ) {}
        }
        Text(
            text = if (isAvailable) "Available" else "Unavailable",
            style = MaterialTheme.typography.caption,
            color = color
        )
    }
} 