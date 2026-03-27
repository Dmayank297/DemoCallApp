package com.example.democallapp.ui.screens.active

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.democallapp.R
import com.example.democallapp.ui.screens.components.*
import com.example.democallapp.ui.theme.DMSansFontFamily
import com.example.democallapp.ui.theme.NightDialColors

@Composable
fun ActiveCallScreen(
    callerName: String,
    callerNumber: String,
    viewModel: ActiveCallViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier.fillMaxSize().background(
            Brush.radialGradient(
                colors = listOf(Color(0xFF071510), NightDialColors.Background),
                radius = 900f
            )
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(Modifier.height(16.dp))
                EncryptedBadge(label = "END-TO-END ENCRYPTED", icon = R.drawable.lock)
                Spacer(Modifier.height(48.dp))

                val initials = callerName.trim().split(" ")
                    .mapNotNull { it.firstOrNull()?.uppercaseChar() }
                    .take(2).joinToString("").ifEmpty { "?" }

                ContactAvatar(initials = initials, size = 110.dp, isPulsing = uiState.isCallActive)
                Spacer(Modifier.height(24.dp))

                Text(text = callerName.ifBlank { callerNumber },
                    color = NightDialColors.OnSurface, fontSize = 26.sp,
                    fontFamily = DMSansFontFamily, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(10.dp))

                AnimatedVisibility(visible = uiState.isCallActive, enter = fadeIn(), exit = fadeOut()) {
                    CallTimerText(elapsedSeconds = uiState.elapsedSeconds)
                }

                if (!uiState.isCallActive) {
                    Text(text = "Connecting...", color = NightDialColors.OnSurfaceMuted,
                        fontSize = 14.sp, fontFamily = DMSansFontFamily)
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    CallActionButton(icon = R.drawable.mic_off, label = "Mute",
                        onClick = viewModel::toggleMute,
                        backgroundColor = if (uiState.isMuted) NightDialColors.ToggleOnBg else NightDialColors.SurfaceVariant,
                        iconTint = if (uiState.isMuted) NightDialColors.OnSurface else NightDialColors.OnSurfaceMuted)
                    CallActionButton(icon = R.drawable.volume_up, label = "Speaker",
                        onClick = viewModel::toggleSpeaker,
                        backgroundColor = if (uiState.isSpeakerOn) NightDialColors.ToggleOnBg else NightDialColors.SurfaceVariant,
                        iconTint = if (uiState.isSpeakerOn) NightDialColors.AccentGreen else NightDialColors.OnSurfaceMuted)
                    CallActionButton(icon = R.drawable.dialpad, label = "Keypad",
                        onClick = {}, backgroundColor = NightDialColors.SurfaceVariant,
                        iconTint = NightDialColors.OnSurfaceMuted)
                }
                Spacer(Modifier.height(32.dp))
                EndCallButton(onClick = { /* system handles this via CALL_STATE_IDLE */ })
                Spacer(Modifier.height(40.dp))
            }
        }
    }
}