package com.vivek.abacusactivity.screens.result

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.semantics.error
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vivek.abacusactivity.R
import com.vivek.abacusactivity.domain.model.ProblemResult
import com.vivek.abacusactivity.screens.components.ResultRow
import com.vivek.abacusactivity.screens.components.ScreenTitle
import com.vivek.abacusactivity.ui.theme.AppTheme

@Composable
fun QuizResultScreen(
    modifier: Modifier,
    score: Int,
    results: List<ProblemResult>,
    onRestart: () -> Unit
) {
    val scoreColor = if (score > results.size / 2) {
        AppTheme.customColors.success
    } else {
        AppTheme.customColors.error
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize() // Use full size for scrolling
    ) {
        ScreenTitle(
            text = stringResource(R.string.times_up),
            modifier = Modifier.padding(top = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(stringResource(R.string.your_final_score), fontSize = 22.sp)
        Text(
            text = "$score",
            fontSize = 60.sp,
            fontWeight = FontWeight.ExtraBold,
            color = scoreColor        )
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
