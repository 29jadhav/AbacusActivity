package com.vivek.abacusactivity.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.vivek.abacusactivity.screens.game.GameActiveScreen
import com.vivek.abacusactivity.screens.game.GameState
import com.vivek.abacusactivity.screens.game.GameViewModel
import com.vivek.abacusactivity.screens.history.HistoryScreen
import com.vivek.abacusactivity.screens.login.LoginScreen
import com.vivek.abacusactivity.screens.result.QuizResultScreen
import com.vivek.abacusactivity.screens.result.ResultViewModel
import com.vivek.abacusactivity.screens.start.StartScreen

@Composable
fun AppNavigation(modifier: Modifier, isUserLoggedIn: Boolean) {
    val navController = rememberNavController()
    val lifecycleOwner = LocalLifecycleOwner.current

    val startDestination = if (isUserLoggedIn) AppRoutes.Home.route else AppRoutes.Login.route

    NavHost(navController = navController, startDestination = startDestination) {

        composable(AppRoutes.Login.route) {
            LoginScreen(
                onSignInSuccess = {
                    // On success, navigate to home and clear the entire back stack
                    // ensuring the user can't go back to the login screen.
                    navController.navigate(AppRoutes.Home.route) {
                        popUpTo(AppRoutes.Login.route) { inclusive = true }
                    }
                }
            )
        }
        composable(AppRoutes.Home.route) {
            // Just call the main screen, which manages its own state via the ViewModel
            StartScreen(
                modifier = modifier,
                onStart = { duration, lesson ->
                    navController.navigate(
                        AppRoutes.Game(duration = duration, lessonId = lesson).buildRoute()
                    )
                },
                onNavigateToHistory = { navController.navigate(AppRoutes.History.route) }
            )
        }

        composable(
            route = AppRoutes.Game().route,
            arguments = listOf(
                navArgument("durationInSeconds") { type = NavType.IntType },
                navArgument("lessonId") { type = NavType.IntType })
        ) {
            val gameViewModel: GameViewModel = hiltViewModel()
            val uiState by gameViewModel.uiState.collectAsState()

            DisposableEffect(lifecycleOwner, gameViewModel) {
                // When this composable enters the screen,
                // add the ViewModel as a lifecycle observer.
                lifecycleOwner.lifecycle.addObserver(gameViewModel)

                // When this composable leaves the screen (onDispose),
                // remove the observer to prevent memory leaks.
                onDispose {
                    lifecycleOwner.lifecycle.removeObserver(gameViewModel)
                }
            }

            LaunchedEffect(uiState.gameState) {
                if (uiState.gameState == GameState.FINISHED) {
                    uiState.finalizedGameId?.let { gameId ->
                        // The 'let' block only executes if finalizedGameId is NOT null.
                        // 'gameId' inside this block is a non-nullable Long.
                        navController.navigate(
                            AppRoutes.Result(gameId = gameId).buildRoute()
                        ) {
                            // Clear the game screen from the back stack to prevent the user
                            // from navigating back to a finished game.
                            popUpTo(AppRoutes.Game().route) { inclusive = true }
                        }
                    }
                }
            }
            // GameActiveScreen displays the current state
            GameActiveScreen(
                modifier = modifier,
                uiState = uiState,
                onEvent = gameViewModel::onEvent,
            )
        }

        composable(
            route = AppRoutes.Result().route,
            arguments = listOf(
                navArgument("gameId") { type = NavType.LongType },
                navArgument("isFromHistory") { type = NavType.BoolType }
            )
        ) { backStackEntry ->
            val cameFromHistory =
                navController.previousBackStackEntry?.destination?.route == AppRoutes.History.route
            val context = LocalContext.current
            val resultViewModel: ResultViewModel = hiltViewModel()
            // Result screen shows final score
            QuizResultScreen(
                modifier = modifier,
                viewModel = resultViewModel,
                isFromHistory = cameFromHistory,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onRestart = {
                    // Navigate back to the start screen
                    navController.navigate(AppRoutes.Home.route) {
                        popUpTo(AppRoutes.Home.route) { inclusive = true }
                    }
                }
            )
        }

        composable(AppRoutes.History.route) {
            HistoryScreen(
                onNavigateBack = { navController.popBackStack() },
                onGameClicked = { gameId ->
                    navController.navigate(AppRoutes.Result(gameId).buildRoute())
                }
            )
        }
    }
}