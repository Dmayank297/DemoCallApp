package com.example.democallapp.ui.theme


import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.democallapp.R

object NightDialColors {
    val Background = Color(0xFF060202)
    val Surface = Color(0xFF141418)
    val SurfaceVariant = Color(0xFF1E1E26)
    val SurfaceElevated = Color(0xFF252530)

    val AccentGreen = Color(0xFF00E676)
    val AccentGreenDim = Color(0xFF00C853)
    val AccentGreenContainer = Color(0xFF003A1F)

    val DestructiveRed = Color(0xFFFF3B30)
    val DestructiveRedContainer = Color(0xFF3A0A08)

    val OnSurface = Color(0xFFEEEEF2)
    val OnSurfaceMuted = Color(0xFF888899)
    val OnSurfaceFaint = Color(0xFFFAFAFB)

    val DialKeyBg = Color(0xFF1E1E28)
    val ToggleActiveBg = Color(0xFF2C2C3A)
    val ToggleOnBg = Color(0xFFF5F5F7)

    val DividerColor = Color(0xFF222230)
}


object LightDialColors {
    val Background = Color(0xFFF8FAF8)
    val Surface = Color(0xFFFFFFFF)
    val SurfaceVariant = Color(0xFFEDEFF0)
    val SurfaceElevated = Color(0xFFF2F4F5)

    val AccentGreen = Color(0xFF00C853)
    val AccentGreenDim = Color(0xFF00A844)
    val AccentGreenContainer = Color(0xFFD8F5E3)

    val DestructiveRed = Color(0xFFD32F2F)
    val DestructiveRedContainer = Color(0xFFFDE0DC)

    val OnSurface = Color(0xFF1A1C1A)
    val OnSurfaceMuted = Color(0xFF6B6F6B)
    val OnSurfaceFaint = Color(0xFF9EA29E)

    val DialKeyBg = Color(0xFFF1F3F4)
    val ToggleActiveBg = Color(0xFFE6E8E9)
    val ToggleOnBg = Color(0xFFFFFFFF)

    val DividerColor = Color(0xFFE0E0E0)
}

val DMSansFontFamily = FontFamily(
    Font(R.font.dm_sans_light, FontWeight.Light),
    Font(R.font.dm_sans_regular, FontWeight.Normal),
    Font(R.font.dm_sans_medium, FontWeight.Medium),
    Font(R.font.dm_sans_semibold, FontWeight.SemiBold),
    Font(R.font.dm_sans_bold, FontWeight.Bold),
)

val NightDialTypography = androidx.compose.material3.Typography(
    displayLarge = TextStyle(
        fontFamily = DMSansFontFamily,
        fontWeight = FontWeight.Light,
        fontSize = 48.sp,
        letterSpacing = (-1).sp
    ),
    displayMedium = TextStyle(
        fontFamily = DMSansFontFamily,
        fontWeight = FontWeight.Light,
        fontSize = 36.sp,
        letterSpacing = (-0.5).sp
    ),
    headlineLarge = TextStyle(
        fontFamily = DMSansFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        letterSpacing = 0.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = DMSansFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp,
        letterSpacing = 0.sp
    ),
    titleLarge = TextStyle(
        fontFamily = DMSansFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        letterSpacing = 0.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = DMSansFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        letterSpacing = 0.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = DMSansFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = DMSansFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 10.sp,
        letterSpacing = 0.5.sp
    )
)

private val NightDialColorScheme = darkColorScheme(
    primary = NightDialColors.AccentGreen,
    onPrimary = Color(0xFF003320),
    primaryContainer = NightDialColors.AccentGreenContainer,
    onPrimaryContainer = NightDialColors.AccentGreen,
    background = NightDialColors.Background,
    onBackground = NightDialColors.OnSurface,
    surface = NightDialColors.Surface,
    onSurface = NightDialColors.OnSurface,
    surfaceVariant = NightDialColors.SurfaceVariant,
    onSurfaceVariant = NightDialColors.OnSurfaceMuted,
    error = NightDialColors.DestructiveRed,
)

private val LightDialColorScheme = lightColorScheme(
    primary = NightDialColors.AccentGreen,
    onPrimary = Color.White,
    primaryContainer = NightDialColors.AccentGreenContainer,
    onPrimaryContainer = Color(0xFF002114),
    background = Color(0xFFF8FAF8),
    onBackground = Color(0xFF1A1C1A),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1A1C1A),
    surfaceVariant = Color(0xFFE1E3DE),
    onSurfaceVariant = Color(0xFF44483F),
    error = NightDialColors.DestructiveRed,
)

@Composable
fun CallAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightDialColorScheme,
        typography = NightDialTypography,
        content = content
    )
}