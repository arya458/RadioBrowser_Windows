package radio.presentation.compose.Search

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp


@Composable
fun SearchView(onclick : (String) -> Unit) {

    val searchText = remember { mutableStateOf("") }

    Surface(
        Modifier
        .width(250.dp)
        .wrapContentHeight()
        .border(1.dp, color = MaterialTheme.colors.primary, shape = RoundedCornerShape(10.dp)),
        shape = RoundedCornerShape(10.dp),
        elevation = 5.dp
    ) {
        TextField(
            searchText.value,
            onValueChange = {
                searchText.value = it
            },
            colors = TextFieldDefaults.textFieldColors(
                textColor = MaterialTheme.colors.onSurface,
                disabledTextColor = Color.Transparent,
                backgroundColor = MaterialTheme.colors.surface,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            maxLines = 1,
            modifier = Modifier
                .width(300.dp)
                .height(50.dp)
                .padding(end = 100.dp)
                .wrapContentHeight()
        )

        Row(
            Modifier
            .fillMaxWidth().wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start) {

            Spacer(Modifier.weight(1f))
            Button(modifier = Modifier.width(100.dp).height(50.dp).clip(RoundedCornerShape(10.dp)), onClick = {
                onclick(searchText.value)
            }){
                Text("Search", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
            }
        }

    }

}