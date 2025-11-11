package com.vivek.abacusactivity.screens.start

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vivek.abacusactivity.R
import com.vivek.abacusactivity.ui.theme.Dimens
import com.vivek.abacusactivity.utils.Constants
import kotlinx.coroutines.flow.collectLatest

@Composable
fun StartScreen(
    modifier: Modifier = Modifier,
    startViewModel: StartViewModel = viewModel(),
    onStart: (Int) -> Unit,
    onNavigateToHistory: () -> Unit
) {
    val uiState by startViewModel.uiState.collectAsState()
    LaunchedEffect(Unit) {
        startViewModel.navigationEvent.collectLatest { timeInSeconds ->
            onStart(timeInSeconds)
        }
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxSize()
            .padding(Dimens.SpacingMedium)
    ) {
        Text(
            stringResource(R.string.timed_challenge),
            fontSize = Dimens.FontSizeExtraLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(Dimens.SpacingLarge))
        Text(
            stringResource(R.string.choose_duration_minutes),
            fontSize = Dimens.FontSizeSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = Dimens.ALPHA_MEDIUM)
        )
        Spacer(modifier = Modifier.height(Dimens.SpacingMedium))

        Row(
            horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingSmall),
            modifier = Modifier.padding(horizontal = Dimens.SpacingMedium)
        ) {
            uiState.predefinedTimes.forEach { time ->
                Button(
                    onClick = { onStart(time * Constants.SECONDS_IN_MINUTE) },
                    modifier = Modifier.weight(Constants.FULL_LAYOUT_WEIGHT)
                ) {
                    Text(stringResource(R.string.duration_minutes, time))
                }
            }
        }
        Spacer(modifier = Modifier.height(Dimens.SpacingLarge))
        Text(stringResource(R.string.custom_time_minutes_label), fontSize = Dimens.FontSizeSmall)
        Spacer(modifier = Modifier.height(Dimens.SpacingMedium))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingSmall)
        ) {
            OutlinedTextField(
                value = uiState.customTimeInput,
                onValueChange = { startViewModel.onEvent(StartScreenEvent.OnCustomTimeChange(it)) },
                label = { Text(stringResource(R.string.minutes_input_label)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.width(Dimens.InputFieldSmall),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    cursorColor = MaterialTheme.colorScheme.primary
                )
            )
            Button(
                onClick = {
                    val time = uiState.customTimeInput.toIntOrNull() ?: Constants.DEFAULT_TIME_VALUE
                    onStart(time * Constants.SECONDS_IN_MINUTE)
                },
                enabled = uiState.isCustomTimeValid
            ) {
                Text(stringResource(R.string.start_button))
            }
        }
        Spacer(modifier = Modifier.height(Dimens.SpacingDoubleExtraLarge))

        OutlinedButton(onClick = onNavigateToHistory) {
            Text(stringResource(R.string.view_history))
        }
    }
}
