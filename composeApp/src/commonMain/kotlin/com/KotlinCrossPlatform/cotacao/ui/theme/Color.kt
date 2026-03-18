package com.KotlinCrossPlatform.cotacao.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color

val DarkBg = Color(0xFF121212)
val DarkSurface = Color(0xFF1E1E1E)
val GoldPrimary = Color(0xFFFFD700)
val GoldVariant = Color(0xFFCFB53B)
val AccentBlue = Color(0xFF2196F3)
val TextPrimary = Color(0xFFFFFFFF)
val TextSecondary = Color(0xFFB0B0B0)
val RedDown = Color(0xFFF44336)
val GreenUp = Color(0xFF4CAF50)

val DarkColorScheme = darkColorScheme(
    primary = GoldPrimary,
    secondary = AccentBlue,
    background = DarkBg,
    surface = DarkSurface,
    onPrimary = Color.Black,
    onSecondary = Color.White,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    surfaceVariant = DarkSurface,
    onSurfaceVariant = TextSecondary
)
