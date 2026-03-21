package com.example.democallapp.ui.screens.dialpad

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.democallapp.R
import com.example.democallapp.ui.screens.components.DialKey
import com.example.democallapp.ui.screens.components.PrimaryCallButton
import com.example.democallapp.ui.theme.DMSansFontFamily
import com.example.democallapp.ui.theme.NightDialColors


private val dialKeys = listOf(
    listOf("1", "2", "3"),
    listOf("4", "5", "6"),
    listOf("7", "8", "9"),
    listOf("*", "0", "#")
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DialPadScreen(
    viewModel: DialPadViewModel,
    onCallInitiated: (String) -> Unit,
    onSimulateIncoming: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF0D1A12),
                        NightDialColors.Background
                    ),
                    radius = 900f
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(48.dp))

            NumberDisplay(
                display = uiState.formattedDisplay,
                isEmpty = uiState.inputNumber.isEmpty()
            )

            Spacer(Modifier.height(36.dp))

            dialKeys.forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    row.forEach { key ->
                        DialKey(
                            key = key,
                            onClick = { viewModel.onKeyPressed(key) }
                        )
                    }
                }
                Spacer(Modifier.height(16.dp))
            }

            Spacer(Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(NightDialColors.SurfaceVariant)
                        .combinedClickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(bounded = true),
                            onClick = onSimulateIncoming
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.video_call),
                        contentDescription = "Simulate Incoming",
                        tint = NightDialColors.OnSurfaceMuted,
                        modifier = Modifier.size(24.dp)
                    )
                }

                PrimaryCallButton(
                    icon = R.drawable.call,
                    contentDescription = "Call",
                    backgroundColor = if (uiState.isCallEnabled)
                        NightDialColors.AccentGreen
                    else
                        NightDialColors.SurfaceVariant,
                    iconTint = if (uiState.isCallEnabled) Color(0xFF003320) else NightDialColors.OnSurfaceFaint,
                    onClick = {
                        if (uiState.isCallEnabled) {
                            onCallInitiated(uiState.inputNumber)
                        }
                    },
                    size = 72.dp,
                    iconSize = 30.dp
                )

                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(
                            if (uiState.inputNumber.isNotEmpty())
                                NightDialColors.SurfaceVariant
                            else
                                Color.Transparent
                        )
                        .combinedClickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(bounded = true),
                            onClick = { viewModel.onBackspace() },
                            onLongClick = { viewModel.onBackspaceLongPress() }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (uiState.inputNumber.isNotEmpty()) {
                        Icon(
                            painter = painterResource(R.drawable.backspace),
                            contentDescription = "Backspace",
                            tint = NightDialColors.OnSurfaceMuted,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NumberDisplay(
    display: String,
    isEmpty: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isEmpty) {
            Text(
                text = "Enter number",
                color = NightDialColors.OnSurfaceFaint,
                fontSize = 20.sp,
                fontFamily = DMSansFontFamily,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Center
            )
        } else {
            Text(
                text = display,
                color = NightDialColors.OnSurface,
                fontSize = if (display.length > 12) 28.sp else 38.sp,
                fontFamily = DMSansFontFamily,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}