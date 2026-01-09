package com.example.shopinventoryapp

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource





val DarkColors = darkColorScheme(
    primary = Color(0xFF7C4DFF),       // Royal purple
    onPrimary = Color.White,

    background = Color(0xFF121212),
    onBackground = Color(0xFFE5E7EB),

    surface = Color(0xFF111827),       // Card bg
    onSurface = Color(0xFFF9FAFB),

    surfaceVariant = Color(0xFF1F2933),
    onSurfaceVariant = Color(0xFF9CA3AF),

    secondary = Color(0xFF22C55E),
    onSecondary = Color.Black,

    error = Color(0xFFEF4444),
    onError = Color.White,

    outline = Color(0xFF374151)
)
val LightColors = lightColorScheme(
    primary = Color(0xFF4F46E5),
    onPrimary = Color.White,

    background = Color(0xFFFAFAFA),
    onBackground = Color(0xFF111827),

    surface = Color.Gray,
    onSurface = Color(0xFF1F2937),

    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = Color(0xFF475569),

    secondary = Color(0xFF22C55E),
    onSecondary = Color.White,

    error = Color(0xFFDC2626),
    onError = Color.White,

    outline = Color(0xFFE5E7EB)
)
