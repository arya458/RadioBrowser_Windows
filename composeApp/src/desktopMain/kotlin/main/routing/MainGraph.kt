package main.routing

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import main.util.AppScreens
import player.presentation.compose.SetupPlayer
import radio.presentation.Page.RadioPage

fun NavGraphBuilder.mainGraph() {



    navigation(startDestination = AppScreens.Main.Loading.route, route = AppScreens.Main.route,
        enterTransition = {

            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Up,
                animationSpec = tween(durationMillis = 400, delayMillis = 200)

            )

        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Down,
                animationSpec = tween(durationMillis = 200, delayMillis = 20)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Up,
                animationSpec = tween(durationMillis = 400, delayMillis = 200)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Down,
                animationSpec = tween(durationMillis = 200, delayMillis = 20)
            )
        }
    ) {

        composable(route = AppScreens.Main.Loading.route) {
            //Loading View
            SetupPlayer()


        }
        composable(route = AppScreens.Main.Home.route) {
            //Home View
            RadioPage()
        }
        composable(route = AppScreens.Main.Top.route) {
            //Top View
        }
        composable(route = AppScreens.Main.Favourite.route) {
            //Favourite View
        }
    }

}