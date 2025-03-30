package radio.presentation.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.seiko.imageloader.rememberImagePainter
import radio.data.api.RadioStation

@Composable
fun RadioItemView(station: RadioStation, onClick: () -> Unit) {
    var isHovered by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(200.dp)
            .shadow(
                elevation = if (isHovered) 8.dp else 4.dp,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() },
        elevation = 0.dp,
        shape = RoundedCornerShape(12.dp),
        backgroundColor = MaterialTheme.colors.surface.copy(alpha = if (isHovered) 0.9f else 1f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Station Image
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colors.surface.copy(alpha = 0.5f))
            ) {
                val painter = rememberImagePainter(
                    station.favicon ?: "",
                    placeholderPainter = {
                        painterResource("drawable/icon.png")
                    }
                )
                Image(
                    painter = if (station.favicon?.isNotEmpty() == true) painter else painterResource("drawable/icon.png"),
                    contentDescription = station.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Station Name
            Text(
                text = station.name,
                style = MaterialTheme.typography.subtitle1.copy(
                    color = MaterialTheme.colors.onSurface,
                    fontWeight = FontWeight.Medium
                ),
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
            
            // Codec Badge
            Surface(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .clip(RoundedCornerShape(16.dp)),
                color = MaterialTheme.colors.primary.copy(alpha = 0.8f),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = station.codec ?: "Unknown",
                    style = MaterialTheme.typography.caption.copy(
                        color = MaterialTheme.colors.onPrimary,
                        fontWeight = FontWeight.Medium
                    ),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}