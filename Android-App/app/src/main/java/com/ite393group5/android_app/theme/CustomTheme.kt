package com.ite393group5.android_app.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF2E7D32),  // Deep Green
    secondary = Color(0xFF1B1B1B),  // Almost Black for contrast
    tertiary = Color(0xFFEEEEEE),  // Light Gray for subtle highlights
    background = Color(0xFFFFFFFF),  // Pure White for a clean look
    surface = Color(0xFFF5F5F5),  // Off-White for slight separation
    onPrimary = Color(0xFF000000),  // Black text/icons for contrast
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF4CAF50),  // Slightly brighter green for visibility
    secondary = Color(0xFF212121),  // Dark Gray for contrast
    tertiary = Color(0xFFBDBDBD),  // Mid Gray for soft highlights
    background = Color(0xFF000000),  // True Black for OLED screens
    surface = Color(0xFF121212),  // Very Dark Gray for depth
    onPrimary = Color(0xFFFFFFFF),  // White text/icons for visibility
)

@Composable
fun CustomTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
){
    val colorScheme = if(darkTheme){
        LightColorScheme
    }else{
        DarkColorScheme
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = MaterialTheme.typography,
    ){
        content()
    }
}
