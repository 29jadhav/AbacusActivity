package com.vivek.abacusactivity.screens.game

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.vivek.abacusactivity.R
import com.vivek.abacusactivity.ui.theme.AppTheme
import com.vivek.abacusactivity.ui.theme.Dimens
import com.vivek.abacusactivity.utils.Constants

@Composable
fun GameActiveScreen(
    modifier: Modifier,
    uiState: GameUiState,
    onEvent: (GameEvent) -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Layer 1: The active game content is always at the bottom.
        // It will be visible whether the game is active or paused.
        ActiveGameContent(
            modifier = Modifier, // The Box handles the main modifier now
            uiState = uiState,
            onEvent = onEvent
        )

        // Layer 2: The PausedScreen overlay, shown only when the game is paused.
        if (uiState.gameState == GameState.PAUSED) {
            PausedScreen(
                modifier,
                onResume = { onEvent(GameEvent.ResumeGame) }
            )
        }
        if (uiState.showTerminateConfirmDialog) {
            AlertDialog(
                onDismissRequest = { onEvent(GameEvent.DismissTerminateDialog) },
                title = { Text(text = stringResource(R.string.terminate_dialog_title)) },
                text = { Text(text = stringResource(R.string.terminate_dialog_message)) },
                confirmButton = {
                    Button(
                        onClick = { onEvent(GameEvent.TerminateGame) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text(stringResource(R.string.terminate_dialog_confirm_button))
                    }
                },
                dismissButton = {
                    Button(onClick = { onEvent(GameEvent.DismissTerminateDialog) }) {
                        Text(stringResource(R.string.cancel_button))
                    }
                }
            )
        }
    }
}

@Composable
fun ActiveGameContent(
    modifier: Modifier,
    uiState: GameUiState,
    onEvent: (GameEvent) -> Unit
) {
    var userAnswer by remember { mutableStateOf("") }
    val problem = uiState.currentProblem

    val defaultTextColor = MaterialTheme.colorScheme.onSurface
    val warningColor = AppTheme.customColors.error // Use the theme-aware error color

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(Dimens.SpacingMedium),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = stringResource(R.string.score_label, uiState.score),
                fontSize = Dimens.FontSizeMedium,
                fontWeight = FontWeight.Bold,
                color = defaultTextColor
            )
            Text(
                text = stringResource(
                    R.string.time_label,
                    uiState.timeRemaining / Constants.SECONDS_IN_MINUTE,
                    uiState.timeRemaining % Constants.SECONDS_IN_MINUTE
                ),
                fontSize = Dimens.FontSizeMedium,
                fontWeight = FontWeight.Bold,
                color = if (uiState.isTimeWarning) warningColor else defaultTextColor
            )
        }

        Spacer(modifier = Modifier.height(Dimens.SpacingDoubleExtraLarge))
        Text(
            text = stringResource(R.string.calculate_the_sum),
            fontSize = Dimens.FontSizeMedium,
            color = defaultTextColor.copy(alpha = Dimens.ALPHA_MEDIUM)
        )
        Spacer(modifier = Modifier.height(Dimens.SpacingExtraLarge))

        AnimatedContent(
            targetState = problem,
            label = stringResource(R.string.problem_number_animation),
            transitionSpec = {
                val slideIn = slideInVertically { fullHeight -> -fullHeight }
                val slideOut = slideOutVertically { fullHeight -> fullHeight }
                slideIn togetherWith slideOut
            }
        ) { currentProblem ->
            Column(
                verticalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                currentProblem?.let { problem ->
                    problem.numbers.forEach { number ->
                        Text(
                            text = number.toString(),
                            fontSize = Dimens.FontSizeTitle,
                            fontWeight = FontWeight.Bold,
                            color = defaultTextColor
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(Dimens.SpacingDoubleExtraLarge))
        OutlinedTextField(
            value = userAnswer,
            onValueChange = { userAnswer = it },
            label = { Text(stringResource(R.string.your_answer_label)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    onEvent(GameEvent.SubmitAnswer(userAnswer))
                    userAnswer = "" // Clear field after submitting
                },
            ),
            singleLine = true,
            modifier = Modifier.width(Dimens.InputFieldMedium),
            colors = TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                cursorColor = MaterialTheme.colorScheme.primary
            )
        )
        Spacer(modifier = Modifier.height(Dimens.SpacingExtraLarge))
        Button(
            onClick = {
                onEvent(GameEvent.SubmitAnswer(userAnswer))
                userAnswer = "" // Clear field after submitting
            },
            enabled = userAnswer.isNotBlank()
        ) {
            Text(stringResource(R.string.submit_answer_button))
        }
        // Spacer pushes the Terminate button to the bottom
        Spacer(Modifier.weight(1f))

        // This is the new Terminate Game button
        Button(
            onClick = { onEvent(GameEvent.RequestTerminateGame) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = Dimens.SpacingLarge),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            )
        ) {
            Text(stringResource(R.string.terminate_game_button))
        }
    }
}
