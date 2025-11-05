package com.vivek.abacusactivity.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vivek.abacusactivity.R

@Composable
fun StartScreen(onStart: (Int) -> Unit) {
    var customTime by remember { mutableStateOf("") }
    val predefinedTimes = listOf(1, 5, 7, 10)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(stringResource(R.string.timed_challenge), fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(24.dp))
        Text(stringResource(R.string.choose_duration_minutes), fontSize = 18.sp)
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            predefinedTimes.forEach { time ->
                Button(
                    onClick = { onStart(time * 60) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(stringResource(R.string.duration_minutes, time))
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(stringResource(R.string.custom_time_minutes_label), fontSize = 18.sp)
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = customTime,
                onValueChange = { customTime = it },
                label = { Text(stringResource(R.string.minutes_input_label)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.width(120.dp)
            )
            Button(
                onClick = { onStart((customTime.toIntOrNull() ?: 0) * 60) },
                enabled = customTime.toIntOrNull() != null && customTime.toIntOrNull()!! > 0
            ) {
                Text(stringResource(R.string.start_button))
            }
        }
    }
}
