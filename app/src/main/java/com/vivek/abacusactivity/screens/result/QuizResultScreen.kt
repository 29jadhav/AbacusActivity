package com.vivek.abacusactivity.screens.result

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vivek.abacusactivity.R
import com.vivek.abacusactivity.domain.model.Problem
import com.vivek.abacusactivity.domain.model.ProblemResult
import com.vivek.abacusactivity.screens.components.AnimatedCounter
import com.vivek.abacusactivity.screens.components.ResultRow
import com.vivek.abacusactivity.ui.theme.AppTheme
import com.vivek.abacusactivity.ui.theme.Dimens
import com.vivek.abacusactivity.utils.Constants
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizResultScreen(
    modifier: Modifier,
    viewModel: ResultViewModel,
    isFromHistory: Boolean,
    onNavigateBack: () -> Unit,
    onRestart: () -> Unit
) {
    val gameDetails by viewModel.gameDetails.collectAsState()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.quiz_results_title)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        // Show a loading indicator while data is being fetched from the database
        if (gameDetails == null) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        val score = gameDetails?.game?.correctAnswers ?: 0
        val results = gameDetails?.problems ?: emptyList()
        val totalQuestions = results.size
        val correctAnswers = results.count { it.isCorrect }
        val incorrectAnswers = totalQuestions - correctAnswers
        val accuracy =
            if (totalQuestions > 0) (correctAnswers.toFloat() / totalQuestions * 100).roundToInt() else 0
        val time = gameDetails?.game?.totalDurationInSeconds?.let {
            "${it / 60}min"
        }


        val scoreColor = if (score > results.size / Constants.MAJORITY_SCORE_DIVISOR) {
            AppTheme.customColors.success
        } else {
            AppTheme.customColors.error
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(Dimens.SpacingMedium))
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(Dimens.SpacingMedium),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(Dimens.SpacingSmall)
                ) {
                    Text(
                        stringResource(R.string.final_score),
                        style = MaterialTheme.typography.titleMedium
                    )
                    AnimatedCounter(
                        count = score,
                        style = MaterialTheme.typography.displayLarge.copy(fontWeight = FontWeight.ExtraBold),
                        color = if (score > totalQuestions / 2) AppTheme.customColors.success else AppTheme.customColors.error
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = Dimens.SpacingSmall),
                        thickness = DividerDefaults.Thickness,
                        color = DividerDefaults.color
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        SummaryStat(
                            stringResource(R.string.correct),
                            "$correctAnswers",
                            AppTheme.customColors.success
                        )
                        SummaryStat(
                            stringResource(R.string.incorrect),
                            "$incorrectAnswers",
                            AppTheme.customColors.error
                        )

                    }
                    Spacer(modifier = Modifier.height(Dimens.SpacingSmall)) // Gap between rows
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        SummaryStat(stringResource(R.string.accuracy), "$accuracy%")

                        // --- ADDED TIME HERE ---
                        SummaryStat(
                            stringResource(R.string.time_played), // "Time"
                            time ?: "" // "60s" or "3m 0s"
                        )
                    }

                }
            }

            Spacer(modifier = Modifier.height(Dimens.SpacingMedium))

            // This LazyColumn will show the detailed results and is scrollable
            LazyColumn(
                modifier = Modifier.weight(Constants.FULL_LAYOUT_WEIGHT) // Takes up available space
            ) {
                items(results) { entity ->
                    // MODIFICATION: Map from ProblemEntity to ProblemResult before passing to the UI component.
                    // We use remember to avoid recalculating this on every recomposition.
                    val result = remember(entity) {
                        val optionNumbers = entity.numbers
                            .removeSurrounding("[", "]")
                            .split(",")
                            .mapNotNull { it.trim().toIntOrNull() }
                        ProblemResult(
                            problem = Problem(
                                // Convert the comma-separated string from the DB back into a list of integers
                                numbers = optionNumbers,
                                correctAnswer = optionNumbers.sum()
                            ),
                            userAnswer = entity.userAnswer,
                            isCorrect = entity.isCorrect
                        )
                    }
                    println("result: $result")
                    ResultRow(result)
                }
            }

            if (!isFromHistory) {
                Spacer(modifier = Modifier.height(Dimens.SpacingMedium))
                Button(
                    onClick = onRestart,
                    modifier = Modifier.padding(bottom = Dimens.SpacingMedium)
                ) {
                    Text(stringResource(R.string.play_again_button))
                }
            }
        }
    }
}


@Composable
private fun SummaryStat(label: String, value: String, valueColor: Color = Color.Unspecified) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, style = MaterialTheme.typography.bodySmall)
        Text(
            value,
            style = MaterialTheme.typography.titleLarge,
            color = valueColor,
            fontWeight = FontWeight.Bold
        )
    }
}

