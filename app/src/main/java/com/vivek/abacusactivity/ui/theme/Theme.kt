package com.vivek.abacusactivity.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// 1. DEFINE the structure for your custom colors
data class CustomColors(
    val success: Color,
    val error: Color,
    val terminatedGame: Color
)

// 2. CREATE CompositionLocals for light and dark themes
private val LocalLightColors = staticCompositionLocalOf {
    CustomColors(
        success = SuccessGreenLight,
        error = ErrorRedLight,
        terminatedGame = TerminatedOrangeLight
    )
}

private val LocalDarkColors = staticCompositionLocalOf {
    CustomColors(
        success = SuccessGreenDark,
        error = ErrorRedDark,
        terminatedGame = TerminatedOrangeDark
    )
}

// Default Light and Dark color schemes...
private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)
private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

// 3. EXPOSE your custom colors through an object
object AppTheme {
    val customColors: CustomColors
        @Composable
        get() = if (isSystemInDarkTheme()) LocalDarkColors.current else LocalLightColors.current
}


// 4. UPDATE your main theme composable
@Composable
fun AbacusActivityTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val customColors = if (darkTheme) LocalDarkColors.current else LocalLightColors.current

    CompositionLocalProvider(
        (if (darkTheme) LocalDarkColors else LocalLightColors) provides customColors
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
