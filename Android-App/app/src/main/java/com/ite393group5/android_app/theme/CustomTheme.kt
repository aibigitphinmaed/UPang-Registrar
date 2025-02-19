package com.ite393group5.android_app.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF388E3C),  // Green - Primary action color
    secondary = Color(0xFFFFC107),  // Yellow - Accents & highlights
    tertiary = Color(0xFFE0E0E0),  // Light Gray - Subtle UI elements

    background = Color(0xFFFFFFFF),  // White - Clean background
    surface = Color(0xFFF5F5F5),  // Light Gray - Slight separation

    onPrimary = Color.White,  // White text/icons on primary
    onSecondary = Color.Black,  // Black for secondary contrast
    onBackground = Color.Black,  // Black text on background
    onSurface = Color.Black,  // Black text/icons on surface

    error = Color(0xFFD32F2F),  // Red - Warnings & errors
    onError = Color.White  // White text/icons for error states
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF4CAF50),  // Lighter Green for visibility in dark mode
    secondary = Color(0xFFFFD54F),  // Softer Yellow for better contrast
    tertiary = Color(0xFF9E9E9E),  // Mid Gray - Subtle highlights

    background = Color(0xFF121212),  // Dark Gray - OLED-friendly
    surface = Color(0xFF1E1E1E),  // Slightly lighter Gray for better separation

    onPrimary = Color.White,  // White text/icons on primary
    onSecondary = Color.Black,  // Black for secondary contrast
    onBackground = Color.White,  // White text on background
    onSurface = Color.White,  // White text/icons on surface

    error = Color(0xFFEF5350),  // Slightly lighter Red for visibility
    onError = Color.White  // White text/icons for error states
)

@Composable
fun CustomTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), // Uses system setting by default
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = MaterialTheme.typography // Keeps default typography
    ) {
        content()
    }
}
