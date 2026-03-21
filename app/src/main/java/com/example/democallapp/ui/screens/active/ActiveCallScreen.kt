package com.example.democallapp.ui.screens.active


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.democallapp.R
import com.example.democallapp.state.CallState
import com.example.democallapp.ui.screens.components.CallActionButton
import com.example.democallapp.ui.screens.components.CallTimerText
import com.example.democallapp.ui.screens.components.ContactAvatar
import com.example.democallapp.ui.screens.components.EncryptedBadge
import com.example.democallapp.ui.screens.components.EndCallButton
import com.example.democallapp.ui.theme.DMSansFontFamily
import com.example.democallapp.ui.theme.NightDialColors


@Composable
fun ActiveCallScreen(
    callState: CallState,
    viewModel: ActiveCallViewModel,
    onEndCall: () -> Unit
) {
    val activeCall = callState as? CallState.Active ?: return
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
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
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(16.dp))

                EncryptedBadge(
                    label = "END-TO-END ENCRYPTED",
                    icon = R.drawable.lock
                )

                Spacer(Modifier.height(48.dp))

                ContactAvatar(
                    initials = activeCall.contact.avatarInitials,
                    size = 110.dp,
                    isPulsing = true
                )

                Spacer(Modifier.height(24.dp))

                Text(
                    text = activeCall.contact.name,
                    color = NightDialColors.OnSurface,
                    fontSize = 26.sp,
                    fontFamily = DMSansFontFamily,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(Modifier.height(10.dp))

                CallTimerText(elapsedSeconds = activeCall.elapsedSeconds)
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    CallActionButton(
                        icon = R.drawable.mic_off,
                        label = "Mute",
                        onClick = viewModel::toggleMute,
                        backgroundColor = if (uiState.isMuted)
                            NightDialColors.ToggleOnBg
                        else
                            NightDialColors.SurfaceVariant,
                        iconTint = if (uiState.isMuted)
                            NightDialColors.OnSurface
                        else
                            NightDialColors.OnSurfaceMuted
                    )

                    CallActionButton(
                        icon = R.drawable.dialpad,
                        label = "Keypad",
                        onClick = viewModel::toggleKeypad,
                        backgroundColor = NightDialColors.SurfaceVariant,
                        iconTint = NightDialColors.OnSurfaceMuted
                    )

                    CallActionButton(
                        icon = R.drawable.volume_up,
                        label = "Speaker",
                        onClick = viewModel::toggleSpeaker,
                        backgroundColor = if (uiState.isSpeakerOn)
                            NightDialColors.ToggleOnBg
                        else
                            NightDialColors.SurfaceVariant,
                        iconTint = if (uiState.isSpeakerOn)
                            NightDialColors.AccentGreen
                        else
                            NightDialColors.OnSurfaceMuted
                    )
                }

                Spacer(Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    CallActionButton(
                        icon = R.drawable.add_call,
                        label = "Add Call",
                        onClick = {},
                        backgroundColor = NightDialColors.SurfaceVariant,
                        iconTint = NightDialColors.OnSurfaceMuted
                    )

                    CallActionButton(
                        icon = R.drawable.contacts,
                        label = "Contacts",
                        onClick = {},
                        backgroundColor = NightDialColors.SurfaceVariant,
                        iconTint = NightDialColors.OnSurfaceMuted
                    )
                }

                Spacer(Modifier.height(32.dp))

                EndCallButton(onClick = onEndCall)

                Spacer(Modifier.height(40.dp))
            }
        }
    }
}