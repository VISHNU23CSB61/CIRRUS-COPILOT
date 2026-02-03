package com.example.triptracker.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalTime

private val DayColorScheme = lightColorScheme(
    primary = Color(0xFF1C6E8C),
    secondary = Color(0xFF4CB963),
    tertiary = Color(0xFFF4B400),
    background = Color(0xFFF7F9FC),
    surface = Color(0xFFFFFFFF),
    onPrimary = Color(0xFFFFFFFF),
    onSecondary = Color(0xFF083C20),
    onBackground = Color(0xFF101828),
    onSurface = Color(0xFF101828),
    primaryContainer = Color(0xFFD7EEF6),
    onPrimaryContainer = Color(0xFF0F2F3A)
)

private val NightColorScheme = darkColorScheme(
    primary = Color(0xFF8BD5F0),
    secondary = Color(0xFF7BE0B8),
    tertiary = Color(0xFFFFD166),
    background = Color(0xFF0C1220),
    surface = Color(0xFF151C2C),
    onPrimary = Color(0xFF0C1C2C),
    onSecondary = Color(0xFF0C1C2C),
    onBackground = Color(0xFFE4E8F3),
    onSurface = Color(0xFFE4E8F3),
    primaryContainer = Color(0xFF20314B),
    onPrimaryContainer = Color(0xFFD8E9F3)
)

private val TripTrackerTypography = androidx.compose.material3.Typography(
    headlineMedium = TextStyle(
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.2.sp
    ),
    titleMedium = TextStyle(
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold
    ),
    bodyMedium = TextStyle(
        fontSize = 15.sp,
        fontWeight = FontWeight.Normal
    ),
    bodySmall = TextStyle(
        fontSize = 13.sp,
        fontWeight = FontWeight.Normal
    ),
    labelLarge = TextStyle(
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium
    )
)

@Immutable
data class TripGradient(
    val background: Brush
)

val LocalTripGradient: ProvidableCompositionLocal<TripGradient> = staticCompositionLocalOf {
    TripGradient(
        background = Brush.verticalGradient(
            listOf(Color(0xFFF6F7FB), Color(0xFFE9F1F8))
        )
    )
}

@Composable
fun TripTrackerTheme(content: @Composable () -> Unit) {
    val isDay = isDayTime()
    val colorScheme = if (isDay) DayColorScheme else NightColorScheme
    val gradient = if (isDay) {
        TripGradient(
            background = Brush.verticalGradient(
                listOf(Color(0xFFF7FBFF), Color(0xFFDCEBFA), Color(0xFFF3F7FF))
            )
        )
    } else {
        TripGradient(
            background = Brush.verticalGradient(
                listOf(Color(0xFF0B1220), Color(0xFF182338), Color(0xFF0E1A2B))
            )
        )
    }

    androidx.compose.runtime.CompositionLocalProvider(LocalTripGradient provides gradient) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = TripTrackerTypography,
            shapes = androidx.compose.material3.Shapes(
                small = RoundedCornerShape(12.dp),
                medium = RoundedCornerShape(16.dp),
                large = RoundedCornerShape(24.dp)
            ),
            content = content
        )
    }
}

private fun isDayTime(): Boolean {
    val now = LocalTime.now()
    return now.isAfter(LocalTime.of(5, 30)) && now.isBefore(LocalTime.of(18, 30))
}
