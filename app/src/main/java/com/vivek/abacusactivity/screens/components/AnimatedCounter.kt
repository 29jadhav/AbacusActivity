package com.vivek.abacusactivity.screens.components

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

@Composable
fun AnimatedCounter(
    count: Int,
    modifier: Modifier = Modifier,
    style: TextStyle,
    color: Color = Color.Unspecified
) {
    var targetCount by remember { mutableIntStateOf(0) }

    val animatedCount by animateIntAsState(
        targetValue = targetCount,
        animationSpec = tween(durationMillis = 1000), // Animate over 1 second
        label = "scoreAnimation"
    )

    LaunchedEffect(count) {
        targetCount = count
    }

    Text(
        text = "$animatedCount",
        modifier = modifier,
        style = style,
        color = color
    )
}