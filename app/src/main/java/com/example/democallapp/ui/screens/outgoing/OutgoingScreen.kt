package com.example.democallapp.ui.screens.outgoing


import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.democallapp.state.CallState
import com.example.democallapp.ui.screens.components.ContactAvatar
import com.example.democallapp.ui.screens.components.EndCallButton
import com.example.democallapp.ui.theme.DMSansFontFamily
import com.example.democallapp.ui.theme.NightDialColors


@Composable
fun OutgoingCallScreen(
    callState: CallState,
    onEndCall: () -> Unit
) {
    val number = (callState as? CallState.Calling)?.number ?: ""

    val infiniteTransition = rememberInfiniteTransition(label = "calling_dots")
    val dotAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(700, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dotAlpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(Color(0xFF061208), NightDialColors.Background),
                    radius = 800f
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(Modifier.height(80.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                ContactAvatar(
                    initials = number.take(2).ifEmpty { "?" },
                    size = 110.dp,
                    isPulsing = false
                )

                Spacer(Modifier.height(24.dp))

                Text(
                    text = number,
                    color = NightDialColors.OnSurface,
                    fontSize = 26.sp,
                    fontFamily = DMSansFontFamily,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(Modifier.height(10.dp))

                Text(
                    text = "Calling",
                    color = NightDialColors.OnSurfaceMuted.copy(alpha = dotAlpha),
                    fontSize = 16.sp,
                    fontFamily = DMSansFontFamily,
                    fontWeight = FontWeight.Normal
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                EndCallButton(onClick = onEndCall)
                Spacer(Modifier.height(12.dp))
                Text(
                    text = "End",
                    color = NightDialColors.OnSurfaceMuted,
                    fontSize = 12.sp,
                    fontFamily = DMSansFontFamily,
                    fontWeight = FontWeight.Medium
                )
                Spacer(Modifier.height(40.dp))
            }
        }
    }
}