package radio.presentation.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.rememberImagePainter
import de.sfuhrm.radiobrowser4j.Station


@Composable
fun RadioItemView(station: Station, onClick: () -> Unit) {

    Card(modifier = Modifier.padding(10.dp).wrapContentHeight().width(100.dp).clickable {
        onClick()
    }, backgroundColor = MaterialTheme.colors.surface, elevation = 5.dp) {

        Column(
            Modifier.fillMaxSize().padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 0.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val painter = rememberImagePainter(
                station.favicon,
                placeholderPainter = {
                    painterResource("drawable/icon.png")
                }
            )
            Image(
                painter = if (station.favicon != "") painter else painterResource("drawable/icon.png"),
                contentDescription = "",
                Modifier.size(100.dp),
                contentScale = ContentScale.FillBounds
            )
            Spacer(Modifier.size(10.dp))
            Text(
                station.name,
                Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(5.dp),
                style = TextStyle(color = MaterialTheme.colors.onSurface),
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.defaultMinSize(10.dp).weight(1f))
            Text(
                station.codec,
                Modifier
                    .background(MaterialTheme.colors.primary,RoundedCornerShape(10.dp,10.dp,0.dp,0.dp))
                    .wrapContentSize()
                    .clip(RoundedCornerShape(10.dp,10.dp,0.dp,0.dp))
                    .padding(5.dp),
                style = MaterialTheme.typography.caption.copy(color = MaterialTheme.colors.onPrimary),
                textAlign = TextAlign.Center
            )
        }

    }
}