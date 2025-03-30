package main.presentation

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.compose.koinInject
import radio.presentation.Page.RadioPage
import player.presentation.viewmodel.PlayerViewModel
import player.presentation.compose.SetupPlayer
import radio.presentation.viewmodel.RadioViewModel

@Composable
fun App() {
    val radioViewModel: RadioViewModel = koinInject()
    val playerViewModel: PlayerViewModel = koinInject()
    val playerState by playerViewModel.playerState.collectAsState()

    MaterialTheme(
        colors = MaterialTheme.colors.copy(
            primary = Color(0xFF2196F3),
            secondary = Color(0xFF03DAC6),
            background = Color(0xFF121212),
            surface = Color(0xFF1E1E1E),
            onPrimary = Color.White,
            onSecondary = Color.Black,
            onBackground = Color.White,
            onSurface = Color.White,
            onError = Color.White,
            error = Color(0xFFCF6679)
        ),
        typography = MaterialTheme.typography.copy(
            body1 = MaterialTheme.typography.body1.copy(
                fontFamily = FontFamily.Default,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color.White
            ),
            button = MaterialTheme.typography.button.copy(
                fontFamily = FontFamily.Default,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            ),
            h1 = MaterialTheme.typography.h1.copy(
                fontFamily = FontFamily.Default,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            ),
            h2 = MaterialTheme.typography.h2.copy(
                fontFamily = FontFamily.Default,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            ),
            h3 = MaterialTheme.typography.h3.copy(
                fontFamily = FontFamily.Default,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            ),
            h4 = MaterialTheme.typography.h4.copy(
                fontFamily = FontFamily.Default,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            ),
            h5 = MaterialTheme.typography.h5.copy(
                fontFamily = FontFamily.Default,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            ),
            h6 = MaterialTheme.typography.h6.copy(
                fontFamily = FontFamily.Default,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            ),
            subtitle1 = MaterialTheme.typography.subtitle1.copy(
                fontFamily = FontFamily.Default,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            ),
            subtitle2 = MaterialTheme.typography.subtitle2.copy(
                fontFamily = FontFamily.Default,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            ),
            caption = MaterialTheme.typography.caption.copy(
                fontFamily = FontFamily.Default,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = Color.White
            ),
            overline = MaterialTheme.typography.overline.copy(
                fontFamily = FontFamily.Default,
                fontSize = 10.sp,
                fontWeight = FontWeight.Normal,
                color = Color.White
            )
        )
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            AnimatedVisibility(
                visible = true,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                if (playerState == null) {
                    Card(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        elevation = 8.dp,
                        backgroundColor = MaterialTheme.colors.surface
                    ) {
                        SetupPlayer(playerViewModel)
                    }
                } else {
                    Card(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        elevation = 8.dp,
                        backgroundColor = MaterialTheme.colors.surface
                    ) {
                        RadioPage(
                            viewModel = radioViewModel,
                            playerViewModel = playerViewModel
                        )
                    }
                }
            }
        }
    }
}
