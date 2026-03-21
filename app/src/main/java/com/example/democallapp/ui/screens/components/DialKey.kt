package com.example.democallapp.ui.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.democallapp.ui.theme.DMSansFontFamily
import com.example.democallapp.ui.theme.NightDialColors


private val dialKeyLetters = mapOf(
    "2" to "ABC", "3" to "DEF",
    "4" to "GHI", "5" to "JKL", "6" to "MNO",
    "7" to "PQRS", "8" to "TUV", "9" to "WXYZ",
    "0" to "+", "*" to "", "#" to ""
)


@Composable
fun DialKey(
    key: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(72.dp)
            .clip(CircleShape)
            .background(NightDialColors.DialKeyBg)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(
                    bounded = true,
                    color = Color.White.copy(alpha = 0.14f)
                ),
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = key,
                color = NightDialColors.OnSurface,
                fontSize = 24.sp,
                fontFamily = DMSansFontFamily,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 24.sp
            )
            val sub = dialKeyLetters[key]
            if (!sub.isNullOrEmpty()) {
                Text(
                    text = sub,
                    color = NightDialColors.OnSurfaceMuted,
                    fontSize = 9.sp,
                    fontFamily = DMSansFontFamily,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}