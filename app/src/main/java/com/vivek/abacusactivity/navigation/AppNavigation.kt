package com.vivek.abacusactivity.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vivek.abacusactivity.domain.repository.ResultsRepository
import com.vivek.abacusactivity.screens.game.GameActiveScreen
import com.vivek.abacusactivity.screens.game.GameState
import com.vivek.abacusactivity.screens.game.GameViewModel
import com.vivek.abacusactivity.screens.game.GameViewModelFactory
import com.vivek.abacusactivity.screens.result.QuizResultScreen
import com.vivek.abacusactivity.screens.start.StartScreen

@Composable
fun AppNavigation(modifier: Modifier) {
    val navController = rememberNavController()
    val lifecycleOwner = LocalLifecycleOwner.current

    NavHost(navController = navController, startDestination = AppRoutes.Start.route) {

        composable(AppRoutes.Start.route) {
            // Just call the main screen, which manages its own state via the ViewModel
            StartScreen(
                modifier = modifier,
                onStart = { duration ->
                    navController.navigate(AppRoutes.Game(duration = duration).buildRoute())
                }
            )
        }

        composable(route = AppRoutes.Game().route, arguments = AppRoutes.Game().navArguments) {
            val duration = it.arguments?.getInt("duration") ?: 60
            val gameViewModel: GameViewModel = viewModel(factory = GameViewModelFactory(duration))
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
                    ResultsRepository.holdResults(uiState.results)
                    // Navigate to results and pass the final score as an argument
                    navController.navigate(AppRoutes.Result(score = uiState.score).buildRoute()) {
                        // Clear the game screen from the back stack
                        popUpTo(AppRoutes.Game().route) { inclusive = true }
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
            AppRoutes.Result().route,
            arguments = AppRoutes.Result().navArguments
        ) { backStackEntry ->
            val score = backStackEntry.arguments?.getInt("score") ?: 0
            // Result screen shows final score
            QuizResultScreen(
                modifier = modifier,
                score = score,
                results = remember { ResultsRepository.retrieveResults()},
                onRestart = {
                    // Navigate back to the start screen
                    navController.navigate(AppRoutes.Start.route) {
                        popUpTo(AppRoutes.Start.route) { inclusive = true }
                    }
                }
            )
        }
    }
}