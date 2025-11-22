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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vivek.abacusactivity.R
import com.vivek.abacusactivity.ui.theme.AbacusActivityTheme
import com.vivek.abacusactivity.ui.theme.Dimens
import com.vivek.abacusactivity.utils.Constants
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartScreen(
    modifier: Modifier = Modifier,
    startViewModel: StartViewModel = viewModel(),
    onStart: (Int, Int) -> Unit,
    onNavigateToHistory: () -> Unit
) {
    val uiState by startViewModel.uiState.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedLesson by remember { mutableIntStateOf(1) }
    LaunchedEffect(Unit) {
        startViewModel.navigationEvent.collectLatest { timeInSeconds ->
            onStart(timeInSeconds, selectedLesson)
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.app_name),
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleLarge
                )
                NavigationDrawerItem(
                    label = { Text(stringResource(R.string.view_history)) },
                    selected = false,
                    icon = { Icon(Icons.Default.History, contentDescription = null) },
                    onClick = {
                        scope.launch { drawerState.close() }
                        onNavigateToHistory()
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
                // Add more drawer items here if needed
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.app_name)) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu"
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues) // Apply Scaffold padding
                    .padding(Dimens.SpacingMedium)
            ) {
                Text(text = "Select Lesson", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(Dimens.SpacingSmall))
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    // Lesson 1 Button
                    FilterChip(
                        selected = selectedLesson == 1,
                        onClick = { selectedLesson = 1 },
                        label = { Text("Lesson 1 (Single Digit)") },
                        leadingIcon = if (selectedLesson == 1) {
                            { Icon(Icons.Filled.Check, contentDescription = null) }
                        } else null
                    )

                    // Lesson 2 Button
                    FilterChip(
                        selected = selectedLesson == 2,
                        onClick = { selectedLesson = 2 },
                        label = { Text("Lesson 2 (Double Digit)") },
                        leadingIcon = if (selectedLesson == 2) {
                            { Icon(Icons.Filled.Check, contentDescription = null) }
                        } else null
                    )
                }
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
                            onClick = {
                                onStart(
                                    time * Constants.SECONDS_IN_MINUTE,
                                    selectedLesson
                                )
                            },
                            modifier = Modifier.weight(Constants.FULL_LAYOUT_WEIGHT)
                        ) {
                            Text(stringResource(R.string.duration_minutes, time))
                        }
                    }
                }
                Spacer(modifier = Modifier.height(Dimens.SpacingLarge))
                Text(
                    stringResource(R.string.custom_time_minutes_label),
                    fontSize = Dimens.FontSizeSmall
                )
                Spacer(modifier = Modifier.height(Dimens.SpacingMedium))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingSmall)
                ) {
                    OutlinedTextField(
                        value = uiState.customTimeInput,
                        onValueChange = {
                            startViewModel.onEvent(
                                StartScreenEvent.OnCustomTimeChange(
                                    it
                                )
                            )
                        },
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
                            val time =
                                uiState.customTimeInput.toIntOrNull()
                                    ?: Constants.DEFAULT_TIME_VALUE
                            onStart(time * Constants.SECONDS_IN_MINUTE, selectedLesson)
                        },
                        enabled = uiState.isCustomTimeValid
                    ) {
                        Text(stringResource(R.string.start_button))
                    }
                }
                Spacer(modifier = Modifier.height(Dimens.SpacingDoubleExtraLarge))

                // Optional: You can keep this button or remove it since it's now in the drawer
                OutlinedButton(onClick = onNavigateToHistory) {
                    Text(stringResource(R.string.view_history))
                }
            }
        }
    }
}

// Make sure to add this object for default padding values or import correctly if using Material3 defaults
object NavigationDrawerItemDefaults {
    val ItemPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp)
}


@Preview(showBackground = true)
@Composable
fun StartScreenPreview() {
    AbacusActivityTheme {
        StartScreen(
            onStart = { duration, lesson -> },
            onNavigateToHistory = {}
        )
    }
}
