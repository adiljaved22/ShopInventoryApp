package com.example.shopinventoryapp

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource


/*val DarkColors = darkColorScheme(

   //  Brand color – buttons, FAB, highlights
   primary = Color(0xFFBB86FC),
   onPrimary = Color.Black, // primary button ke upar text/icon

   //  App ki poori screen ka background
   background = Color(0xFF121212),
   onBackground = Color.White, // screen text

   //  Cards, sheets, list items
   surface = Color.Green,        //  background se thora lighter
   onSurface = Color(0xFFEDEDED),      // card ke andar text

   // Secondary card / grouped content
   surfaceVariant = Color(0xFF2A2A2A),
   onSurfaceVariant = Color(0xFFB3B3B3),

   //  Secondary actions
   secondary = Color(0xFFDCF3F0),
   onSecondary = Color.Black,

   // Errors
   error = Color(0xFFCF6679),
   onError = Color.Black,

   // Borders  dividers
   outline = Color(0xFF3C3C3C)
)

val LightColors = lightColorScheme(

   //  Brand color
   primary = Color(0xFF6200EE),
   onPrimary = Color.White,

   //  App background (pure white nahi)
   background = Color(0xFFFAFAFA),
   onBackground = Color(0xFF1C1C1C),

   //  Cards
   surface = Color.White,
   onSurface = Color(0xFF2E2E2E),

   //  Grouped sections
   surfaceVariant = Color(0xFFF1F1F1),
   onSurfaceVariant = Color(0xFF6B6B6B),

   //  Secondary
   secondary = Color(0xFF4CAF50),
   onSecondary = Color.White,

   //  Errors
   error = Color(0xFFB00020),
   onError = Color.White,

   //  Borders
   outline = Color(0xFFDDDDDD)
)*/


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
