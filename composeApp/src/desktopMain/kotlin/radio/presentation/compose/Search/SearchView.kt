package radio.presentation.compose.Search

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SearchView(onclick: (String) -> Unit) {
    var searchText by remember { mutableStateOf("") }
    var isFocused by remember { mutableStateOf(false) }
    var buttonScale by remember { mutableStateOf(1f) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .shadow(
                elevation = if (isFocused) 8.dp else 4.dp,
                shape = RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colors.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Search Icon
            IconButton(
                onClick = { onclick(searchText) },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = MaterialTheme.colors.primary
                )
            }

            // Search TextField
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
                    .onFocusChanged { isFocused = it.isFocused },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = MaterialTheme.colors.onSurface,
                    backgroundColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                ),
                placeholder = {
                    Text(
                        "Search radio stations...",
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                        fontSize = 16.sp
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { onclick(searchText) })
            )

            // Search Button
            Button(
                onClick = { onclick(searchText) },
                modifier = Modifier
                    .padding(end = 8.dp)
                    .scale(buttonScale)
                    .clip(RoundedCornerShape(8.dp)),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.primary,
                    contentColor = MaterialTheme.colors.onPrimary
                )
            ) {
                Text(
                    "Search",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    fontSize = 16.sp
                )
            }
        }
    }
}