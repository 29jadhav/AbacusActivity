package com.vivek.abacusactivity.ui.theme

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * A central object to hold dimensions, typography sizes, and other UI constants
 * used throughout the app. This promotes a consistent design system and makes
 * UI tweaks easier.
 */
object Dimens {

    // === Spacing & Padding ===
    val SpacingSmall = 8.dp
    val SpacingMedium = 16.dp
    val SpacingLarge = 24.dp
    val SpacingExtraLarge = 32.dp
    val SpacingDoubleExtraLarge = 48.dp

    // === Component Sizes ===
    val InputFieldSmall = 120.dp
    val InputFieldMedium = 200.dp
    val IconSizeMedium = 24.dp

    // === Font Sizes ===
    val FontSizeSmall = 18.sp
    val FontSizeMedium = 20.sp
    val FontSizeMediumLarge = 22.sp
    val FontSizeLarge = 24.sp
    val FontSizeExtraLarge = 28.sp
    val FontSizeTitle = 36.sp
    val FontSizeDisplay = 60.sp

    // === Opacity ===
    const val ALPHA_MEDIUM = 0.8f
    const val ALPHA_LOW = 0.6f
}