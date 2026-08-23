package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
  primary = BdiptvGreenPrimary,
  onPrimary = Color.Black,
  primaryContainer = BdiptvGreenDark,
  onPrimaryContainer = Color.White,
  secondary = BdiptvCyan,
  onSecondary = Color.Black,
  secondaryContainer = DarkSurfaceElevated,
  onSecondaryContainer = Color.White,
  tertiary = BdiptvRedAccent,
  onTertiary = Color.White,
  background = DarkBackground,
  onBackground = TextPrimaryDark,
  surface = DarkSurface,
  onSurface = TextPrimaryDark,
  surfaceVariant = DarkSurfaceElevated,
  onSurfaceVariant = TextSecondaryDark,
  outline = DarkSurfaceBorder
)

private val LightColorScheme = lightColorScheme(
  primary = BdiptvGreenDark,
  onPrimary = Color.White,
  primaryContainer = Color(0xFFE8F5E9),
  onPrimaryContainer = Color(0xFF1B5E20),
  secondary = Color(0xFF0284C7),
  onSecondary = Color.White,
  secondaryContainer = Color(0xFFE0F2FE),
  onSecondaryContainer = Color(0xFF0369A1),
  tertiary = BdiptvRedAccent,
  onTertiary = Color.White,
  background = LightBackground,
  onBackground = TextPrimaryLight,
  surface = LightSurface,
  onSurface = TextPrimaryLight,
  surfaceVariant = LightSurfaceElevated,
  onSurfaceVariant = TextSecondaryLight,
  outline = LightSurfaceBorder
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true, // Default to sleek IPTV Dark mode
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
