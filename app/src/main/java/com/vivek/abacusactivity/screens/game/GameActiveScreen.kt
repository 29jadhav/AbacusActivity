package com.vivek.abacusactivity.screens.game

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.error
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vivek.abacusactivity.R
import com.vivek.abacusactivity.screens.game.GameEvent
import com.vivek.abacusactivity.ui.theme.AppTheme

@Composable
fun GameActiveScreen(
    modifier: Modifier,
    uiState: GameUiState,
    onEvent: (GameEvent) -> Unit
) {
    var userAnswer by remember { mutableStateOf("") }
    val problem = uiState.problem

    val defaultTextColor = MaterialTheme.colorScheme.onSurface
    val warningColor = AppTheme.customColors.error // Use the theme-aware error color


    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = stringResource(R.string.score_label, uiState.score),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = defaultTextColor
            )
            Text(
                text = stringResource(
                    R.string.time_label,
                    uiState.timeRemaining / 60,
                    uiState.timeRemaining % 60
                ),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = if (uiState.isTimeWarning) warningColor else defaultTextColor
            )
        }

        Spacer(modifier = Modifier.height(48.dp))
        Text(
            text = stringResource(R.string.calculate_the_sum),
            fontSize = 20.sp,
            color = defaultTextColor.copy(alpha = 0.8f)
        )
        Spacer(modifier = Modifier.height(32.dp))

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
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                currentProblem.numbers.forEach { number ->
                    Text(
                        text = number.toString(),
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = defaultTextColor
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(48.dp))
        OutlinedTextField(
            value = userAnswer,
            onValueChange = { userAnswer = it },
            label = { Text(stringResource(R.string.your_answer_label)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            keyboardActions = KeyboardActions(onDone = {
                onEvent(GameEvent.SubmitAnswer(userAnswer))
                userAnswer = "" // Clear field after submitting
            }),
            singleLine = true,
            modifier = Modifier.width(200.dp),
            colors = TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                cursorColor = MaterialTheme.colorScheme.primary
            )
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = {
                onEvent(GameEvent.SubmitAnswer(userAnswer))
                userAnswer = "" // Clear field after submitting
            },
            enabled = userAnswer.isNotBlank()
        ) {
            Text(stringResource(R.string.submit_answer_button))
        }
    }
}
