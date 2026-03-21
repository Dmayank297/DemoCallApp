package com.example.democallapp.ui.screens.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.democallapp.ui.theme.DMSansFontFamily
import com.example.democallapp.ui.theme.NightDialColors

@Composable
fun ContactAvatar(
    initials: String,
    size: Dp = 100.dp,
    isPulsing: Boolean = false,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isPulsing) 1.12f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        if (isPulsing) {
            Box(
                modifier = Modifier
                    .size(size + 24.dp)
                    .scale(pulseScale)
                    .clip(CircleShape)
                    .background(NightDialColors.AccentGreenContainer)
            )
        }

        Box(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .background(NightDialColors.SurfaceVariant)
                .border(
                    width = 2.dp,
                    color = if (isPulsing) NightDialColors.AccentGreen else NightDialColors.SurfaceElevated,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = initials,
                color = NightDialColors.OnSurface,
                fontSize = (size.value * 0.28f).sp,
                fontFamily = DMSansFontFamily,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun CallTimerText(
    elapsedSeconds: Int,
    modifier: Modifier = Modifier
) {
    val minutes = elapsedSeconds / 60
    val seconds = elapsedSeconds % 60
    val formatted = "%02d:%02d".format(minutes, seconds)

    androidx.compose.foundation.layout.Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(NightDialColors.AccentGreen)
        )
        androidx.compose.foundation.layout.Spacer(Modifier.size(8.dp))
        Text(
            text = formatted,
            color = NightDialColors.AccentGreen,
            fontSize = 22.sp,
            fontFamily = DMSansFontFamily,
            fontWeight = FontWeight.Medium,
            letterSpacing = 2.sp
        )
    }
}