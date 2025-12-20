package com.example.shopinventoryapp

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import com.example.shopinventoryapp.ui.theme.Typography

@Composable
fun MyAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), // This function checks the system setting
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColors
    } else {
        LightColors
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Assuming Typography and Shape are defined elsewhere
    /*    shapes = Shapes,*/
        content = content
    )
}