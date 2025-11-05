package com.vivek.abacusactivity.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vivek.abacusactivity.R

@Composable
fun QuizResultScreen(score: Int, results: List<ProblemResult>, onRestart: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize() // Use full size for scrolling
    ) {
        Text(
            stringResource(R.string.times_up),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(stringResource(R.string.your_final_score), fontSize = 22.sp)
        Text(
            text = "$score",
            fontSize = 60.sp,
            fontWeight = FontWeight.ExtraBold,
            color = if (score > results.size / 2) Color(0xFF006400) else Color.Red
        )
        Spacer(modifier = Modifier.height(16.dp))

        // This LazyColumn will show the detailed results and is scrollable
        LazyColumn(
            modifier = Modifier.weight(1f) // Takes up available space
        ) {
            items(results) { result ->
                ResultRow(result)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRestart, modifier = Modifier.padding(bottom = 16.dp)) {
            Text(stringResource(R.string.play_again_button))
        }
    }
}

@Composable
fun ResultRow(result: ProblemResult) {
    val resultColor = if (result.isCorrect) Color(0xFF006400) else Color.Red
    val numbersString = result.problem.numbers.joinToString(" + ")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Left side: The question
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(R.string.question_details, numbersString, result.problem.sum),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(R.string.your_answer_details, result.userAnswer),
                color = resultColor
            )
        }

        // Right side: The result icon (tick or cross)
        Text(
            text = if (result.isCorrect) stringResource(R.string.correct_symbol) else stringResource(R.string.incorrect_symbol),
            fontSize = 24.sp,
            color = resultColor,
            fontWeight = FontWeight.Bold
        )
    }
}
