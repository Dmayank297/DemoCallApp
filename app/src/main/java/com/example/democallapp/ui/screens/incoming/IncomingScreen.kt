package com.example.democallapp.ui.screens.incoming


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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.democallapp.R
import com.example.democallapp.state.CallState
import com.example.democallapp.ui.screens.components.AcceptCallButton
import com.example.democallapp.ui.screens.components.ContactAvatar
import com.example.democallapp.ui.screens.components.EncryptedBadge
import com.example.democallapp.ui.screens.components.EndCallButton
import com.example.democallapp.ui.theme.DMSansFontFamily
import com.example.democallapp.ui.theme.NightDialColors


@Composable
fun IncomingCallScreen(
    callState: CallState,
    onAccept: () -> Unit,
    onReject: () -> Unit
) {
    val contact = (callState as? CallState.Ringing)?.contact ?: return

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(Color(0xFF061208), NightDialColors.Background),
                    radius = 1000f
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
            Spacer(Modifier.height(60.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                ContactAvatar(
                    initials = contact.avatarInitials,
                    size = 110.dp,
                    isPulsing = true
                )

                Spacer(Modifier.height(28.dp))

                Text(
                    text = contact.name,
                    color = NightDialColors.OnSurface,
                    fontSize = 28.sp,
                    fontFamily = DMSansFontFamily,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "Mobile",
                        color = NightDialColors.OnSurfaceMuted,
                        fontSize = 14.sp,
                        fontFamily = DMSansFontFamily,
                        fontWeight = FontWeight.Normal
                    )
                    Text(
                        text = "•",
                        color = NightDialColors.OnSurfaceFaint,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Incoming...",
                        color = NightDialColors.AccentGreen,
                        fontSize = 14.sp,
                        fontFamily = DMSansFontFamily,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(Modifier.height(24.dp))

                EncryptedBadge()
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        EndCallButton(onClick = onReject)
                        Spacer(Modifier.height(10.dp))
                        Text(
                            text = "Decline",
                            color = NightDialColors.OnSurfaceMuted,
                            fontSize = 13.sp,
                            fontFamily = DMSansFontFamily,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        AcceptCallButton(onClick = onAccept)
                        Spacer(Modifier.height(10.dp))
                        Text(
                            text = "Accept",
                            color = NightDialColors.OnSurfaceMuted,
                            fontSize = 13.sp,
                            fontFamily = DMSansFontFamily,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(Modifier.height(32.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    QuickAction(icon = R.drawable.message, label = "MESSAGE")
                    QuickAction(icon = R.drawable.alarm, label = "REMIND")
                }

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun QuickAction(
    icon: Int,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = label,
            tint = NightDialColors.OnSurfaceFaint,
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = label,
            color = NightDialColors.OnSurfaceFaint,
            fontSize = 10.sp,
            fontFamily = DMSansFontFamily,
            fontWeight = FontWeight.Medium,
            letterSpacing = 1.sp
        )
    }
}