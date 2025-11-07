package com.vivek.abacusactivity.screens.components

// In new file: Texts.kt

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import com.vivek.abacusactivity.ui.theme.Dimens

// ...

@Composable
fun ScreenTitle(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        fontSize = Dimens.FontSizeExtraLarge,
        fontWeight = FontWeight.Bold,
        modifier = modifier
    )
}