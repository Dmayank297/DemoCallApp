package com.example.democallapp.ui.screens.calllogs

import android.Manifest
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.democallapp.R
import com.example.democallapp.domain.model.CallLogEntry
import com.example.democallapp.domain.model.CallType
import com.example.democallapp.ui.theme.DMSansFontFamily
import com.example.democallapp.ui.theme.NightDialColors

@Composable
fun CallLogsScreen(viewModel: CallLogsViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val pendingIntent = remember { mutableStateOf<Intent?>(null) }
    val callPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            pendingIntent.value?.let { context.startActivity(it) }
        }

    }
    val phoneStatePermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            viewModel.onPhoneStatePermissionGranted()
        }
    }

    val logsPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> if (granted) viewModel.onCallLogPermissionGranted() }

    LaunchedEffect(Unit) {
        logsPermissionLauncher.launch(Manifest.permission.READ_CALL_LOG)
        phoneStatePermissionLauncher.launch(Manifest.permission.READ_PHONE_STATE)
    }


    LaunchedEffect(Unit) {
        viewModel.callIntent.collect { intent ->
            pendingIntent.value = intent
            callPermissionLauncher.launch(Manifest.permission.CALL_PHONE)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NightDialColors.Background)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Text(
            text = "Recents",
            color = NightDialColors.OnSurface,
            fontSize = 28.sp,
            fontFamily = DMSansFontFamily,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp)
        )

        when {
            uiState.isLoading -> Box(Modifier.fillMaxSize(), Alignment.Center) {
                CircularProgressIndicator(color = NightDialColors.AccentGreen)
            }
            !(uiState.hasCallLogPermission && uiState.hasPhoneStatePermission) -> Box(Modifier.fillMaxSize(), Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("Call log permission required",
                        color = NightDialColors.OnSurfaceMuted, fontFamily = DMSansFontFamily)
                    Button(onClick = { logsPermissionLauncher.launch(Manifest.permission.READ_CALL_LOG) },
                        colors = ButtonDefaults.buttonColors(containerColor = NightDialColors.AccentGreen)) {
                        Text("Grant Access", color = Color(0xFF003320), fontFamily = DMSansFontFamily)
                    }
                }
            }
            uiState.logs.isEmpty() -> Box(Modifier.fillMaxSize(), Alignment.Center) {
                Text("No call history", color = NightDialColors.OnSurfaceMuted, fontFamily = DMSansFontFamily)
            }
            else -> LazyColumn {
                items(uiState.logs, key = { it.id }) { log ->
                    CallLogItem(entry = log, onClick = { viewModel.onLogClicked(log) })
                }
            }
        }
    }
}

@Composable
private fun CallLogItem(entry: CallLogEntry, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = NightDialColors.AccentGreen.copy(alpha = 0.1f))
            ) { onClick() }
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier.size(46.dp).clip(CircleShape)
                .background(NightDialColors.SurfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Text(text = entry.avatarInitials, color = NightDialColors.OnSurface,
                fontSize = 16.sp, fontFamily = DMSansFontFamily, fontWeight = FontWeight.Medium)
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(text = entry.displayName, color = NightDialColors.OnSurface,
                fontSize = 15.sp, fontFamily = DMSansFontFamily, fontWeight = FontWeight.Medium)
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                val icon = when (entry.type) {
                    CallType.INCOMING -> R.drawable.call
                    CallType.OUTGOING -> R.drawable.call
                    CallType.MISSED -> R.drawable.call_end
                    CallType.UNKNOWN -> R.drawable.call
                }
                Icon(painterResource(icon), contentDescription = null,
                    tint = entry.type.color(), modifier = Modifier.size(12.dp))
                Text(text = entry.type.label(), color = entry.type.color(),
                    fontSize = 12.sp, fontFamily = DMSansFontFamily)
                Text("•", color = NightDialColors.OnSurfaceFaint, fontSize = 12.sp)
                Text(text = entry.dateMs.toRelativeDateString(),
                    color = NightDialColors.OnSurfaceMuted, fontSize = 12.sp, fontFamily = DMSansFontFamily)
            }
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(text = entry.durationSeconds.toCallDurationString(),
                color = NightDialColors.OnSurfaceMuted, fontSize = 12.sp, fontFamily = DMSansFontFamily)
        }
    }
}