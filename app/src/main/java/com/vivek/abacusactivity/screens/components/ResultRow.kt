package com.vivek.abacusactivity.screens.components


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.error
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vivek.abacusactivity.R
import com.vivek.abacusactivity.domain.model.ProblemResult
import com.vivek.abacusactivity.ui.theme.AppTheme


@Composable

fun ResultRow(result: ProblemResult) {
    val resultColor = if (result.isCorrect) {
        AppTheme.customColors.success
    } else {
        AppTheme.customColors.error
    }
    val defaultTextColor = MaterialTheme.colorScheme.onSurface
    val numbersString = result.problem.numbers.joinToString(" + ")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(R.string.question_details, numbersString, result.problem.sum),
                fontWeight = FontWeight.Bold,
                color = defaultTextColor
            )
            Text(
                text = stringResource(R.string.your_answer_details, result.userAnswer),
                color = resultColor
            )
        }

        Text(
            text = if (result.isCorrect) stringResource(R.string.correct_symbol) else stringResource(
                R.string.incorrect_symbol
            ),
            fontSize = 24.sp,
            color = resultColor,
            fontWeight = FontWeight.Bold
        )
    }
}