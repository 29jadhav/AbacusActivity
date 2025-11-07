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
import androidx.compose.ui.text.font.FontWeight
import com.vivek.abacusactivity.R
import com.vivek.abacusactivity.domain.model.ProblemResult
import com.vivek.abacusactivity.ui.theme.AppTheme
import com.vivek.abacusactivity.ui.theme.Dimens
import com.vivek.abacusactivity.utils.Constants

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
            .padding(horizontal = Dimens.SpacingMedium, vertical = Dimens.SpacingSmall),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(Constants.FULL_LAYOUT_WEIGHT)) {
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
            fontSize = Dimens.FontSizeLarge,
            color = resultColor,
            fontWeight = FontWeight.Bold
        )
    }
}