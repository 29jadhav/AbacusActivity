package com.vivek.abacusactivity.screens

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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vivek.abacusactivity.R
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith

@Composable
fun GameActiveScreen(
    problem: CalculationProblem,
    score: Int,
    timeRemaining: Int,
    userAnswer: String,
    onAnswerChange: (String) -> Unit,
    onSubmit: () -> Unit
) {
    // This Column will wrap the entire screen content for proper alignment and spacing
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = stringResource(R.string.score_label, score),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(R.string.time_label, timeRemaining / 60, timeRemaining % 60),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = if (timeRemaining <= 10) Color.Red else Color.Black
            )
        }

        Spacer(modifier = Modifier.height(48.dp))
        Text(text = stringResource(R.string.calculate_the_sum), fontSize = 20.sp)
        Spacer(modifier = Modifier.height(32.dp))

        // AnimatedContent will animate its children when the 'problem' state changes.
        AnimatedContent(
            targetState = problem,
            label = stringResource(R.string.problem_number_animation),
            transitionSpec = {
                // Animation for new numbers entering the screen
                val slideIn = slideInVertically { fullHeight -> -fullHeight }
                // Animation for old numbers leaving the screen
                val slideOut = slideOutVertically { fullHeight -> fullHeight }

                // Combine the enter and exit animations
                slideIn togetherWith slideOut
            }
        ) { currentProblem ->
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                currentProblem.numbers.forEach { number ->
                    Text(text = number.toString(), fontSize = 36.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
        Spacer(modifier = Modifier.height(48.dp))
        OutlinedTextField(
            value = userAnswer,
            onValueChange = onAnswerChange,
            label = { Text(stringResource(R.string.your_answer_label)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            keyboardActions = KeyboardActions(onDone = { onSubmit() }),
            singleLine = true,
            modifier = Modifier.width(200.dp)
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onSubmit, enabled = userAnswer.isNotBlank()) {
            Text(stringResource(R.string.submit_answer_button))
        }
    }
}
