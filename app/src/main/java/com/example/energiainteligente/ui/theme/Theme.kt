package com.example.energiainteligente.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = NeonGreen,
    onPrimary = OledBlack,
    primaryContainer = DarkGreenContainer,
    onPrimaryContainer = NeonGreen,
    background = OledBlack,
    onBackground = TextPrimary,
    surface = DarkCardBackground,
    onSurface = TextPrimary,
    onSurfaceVariant = TextSecondary,
    outline = DarkCardBorder
)

@Composable
fun EnergiaInteligenteTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}