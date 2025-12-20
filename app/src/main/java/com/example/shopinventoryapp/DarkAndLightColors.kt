package com.example.shopinventoryapp

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color


val DarkColors = darkColorScheme(

   //  Brand color – buttons, FAB, highlights
   primary = Color(0xFFBB86FC),
   onPrimary = Color.Black, // primary button ke upar text/icon

   //  App ki poori screen ka background
   background = Color(0xFF121212),
   onBackground = Color.White, // screen text

   //  Cards, sheets, list items
   surface = Color(0xFF1E1E1E),        //  background se thora lighter
   onSurface = Color(0xFFEDEDED),      // card ke andar text

   // Secondary card / grouped content
   surfaceVariant = Color(0xFF2A2A2A),
   onSurfaceVariant = Color(0xFFB3B3B3),

   //  Secondary actions
   secondary = Color(0xFF03DAC6),
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
   secondary = Color(0xFF018786),
   onSecondary = Color.White,

   //  Errors
   error = Color(0xFFB00020),
   onError = Color.White,

   //  Borders
   outline = Color(0xFFDDDDDD)
)