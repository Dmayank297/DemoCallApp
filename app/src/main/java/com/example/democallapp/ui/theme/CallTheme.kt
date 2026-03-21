package com.example.democallapp.ui.theme


import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.democallapp.R

object NightDialColors {
    val Background = Color(0xFF0A0A0C)
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
    val OnSurfaceFaint = Color(0xFF444455)

    val DialKeyBg = Color(0xFF1E1E28)
    val ToggleActiveBg = Color(0xFF2C2C3A)
    val ToggleOnBg = Color(0xFF3A3A48)

    val DividerColor = Color(0xFF222230)
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

@Composable
fun CallAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = NightDialColorScheme,
        typography = NightDialTypography,
        content = content
    )
}