package main.presentation

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import main.routing.mainGraph
import main.util.AppScreens
import org.koin.java.KoinJavaComponent.getKoin
import player.presentation.viewmodel.PlayerViewModel
import player.util.PlayerState

@Composable
fun App(player: PlayerViewModel = getKoin().get()) {
    MaterialTheme {
        val playerState by player.playerState.collectAsState()

        val navController: NavHostController = rememberNavController()



        val backStackEntry by navController.currentBackStackEntryAsState()
        // Get the name of the current screen



        Scaffold(
            topBar = {
//                CupcakeAppBar(
//                    currentScreen = currentScreen,
//                    canNavigateBack = navController.previousBackStackEntry != null,
//                    navigateUp = { navController.navigateUp() }
//                )
            }
        ) { innerPadding ->

            NavHost(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                navController = navController,

                startDestination = AppScreens.Main.route
            ) {

                //todo:fix toolbar Collapse Lagggggg !!!!
                mainGraph()
            }
        }

        when (playerState) {

            is PlayerState.Empty -> {
                navController.navigate(AppScreens.Main.Loading.route)
            }

            else -> {
                navController.navigate(AppScreens.Main.Home.route)
            }
        }
    }
}
