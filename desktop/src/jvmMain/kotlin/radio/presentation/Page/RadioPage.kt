package radio.presentation.Page

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.koin.java.KoinJavaComponent
import radio.presentation.compose.RadioList
import radio.presentation.compose.Search.SearchView
import radio.presentation.viewmodel.RadioViewModel


@Composable
fun RadioPage(radioViewModel: RadioViewModel = KoinJavaComponent.getKoin().get()) {


    Surface(Modifier.fillMaxSize(), color = MaterialTheme.colors.surface) {
        RadioList()
        Column(Modifier
            .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top) {
            Spacer(Modifier.size(10.dp))
            SearchView { radioViewModel.search(it) }
        }
    }

}