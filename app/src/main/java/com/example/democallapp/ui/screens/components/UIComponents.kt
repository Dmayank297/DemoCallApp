package com.example.democallapp.ui.screens.components


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.democallapp.R
import com.example.democallapp.ui.theme.DMSansFontFamily
import com.example.democallapp.ui.theme.NightDialColors


@Composable
fun CallActionButton(
    icon: Int,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = NightDialColors.SurfaceVariant,
    iconTint: Color = NightDialColors.OnSurface,
    size: Dp = 64.dp,
    iconSize: Dp = 26.dp
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .background(backgroundColor)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(bounded = true, color = Color.White.copy(alpha = 0.12f)),
                    onClick = onClick
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = label,
                tint = iconTint,
                modifier = Modifier.size(iconSize)
            )
        }
        Text(
            text = label,
            color = NightDialColors.OnSurfaceMuted,
            fontSize = 12.sp,
            fontFamily = DMSansFontFamily,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun PrimaryCallButton(
    icon: Int,
    contentDescription: String,
    backgroundColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconTint: Color = Color.White,
    size: Dp = 72.dp,
    iconSize: Dp = 32.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = true, color = Color.White.copy(alpha = 0.2f)),
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = contentDescription,
            tint = iconTint,
            modifier = Modifier.size(iconSize)
        )
    }
}

@Composable
fun EndCallButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    PrimaryCallButton(
        icon = R.drawable.call_end,
        contentDescription = "End Call",
        backgroundColor = NightDialColors.DestructiveRed,
        onClick = onClick,
        modifier = modifier
    )
}

@Composable
fun AcceptCallButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    PrimaryCallButton(
        icon = R.drawable.call,
        contentDescription = "Accept Call",
        backgroundColor = NightDialColors.AccentGreen,
        iconTint = Color(0xFF003320),
        onClick = onClick,
        modifier = modifier
    )
}