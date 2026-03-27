package com.example.democallapp.ui.screens.contacts


import android.Manifest
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.democallapp.domain.model.Contact
import com.example.democallapp.ui.theme.DMSansFontFamily
import com.example.democallapp.ui.theme.NightDialColors

@Composable
fun ContactsScreen(viewModel: ContactsViewModel = hiltViewModel()) {
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

    val contactsPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) viewModel.onPermissionGranted()
    }

    LaunchedEffect(Unit) {
        contactsPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
    }

    LaunchedEffect(Unit) {
        viewModel.callIntent.collect { intent ->
            callPermissionLauncher.launch(Manifest.permission.CALL_PHONE)
            pendingIntent.value = intent
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NightDialColors.Background)
            .statusBarsPadding()
    ) {
        Text(
            text = "Contacts",
            color = NightDialColors.OnSurface,
            fontSize = 28.sp,
            fontFamily = DMSansFontFamily,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp)
        )

        OutlinedTextField(
            value = uiState.searchQuery,
            onValueChange = viewModel::onSearchQueryChanged,
            placeholder = {
                Text("Search contacts", color = NightDialColors.OnSurfaceFaint,
                    fontFamily = DMSansFontFamily, fontSize = 14.sp)
            },
            leadingIcon = {
                Icon(painterResource(R.drawable.contacts), contentDescription = null,
                    tint = NightDialColors.OnSurfaceMuted, modifier = Modifier.size(18.dp))
            },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = NightDialColors.SurfaceVariant,
                focusedBorderColor = NightDialColors.AccentGreen,
                cursorColor = NightDialColors.AccentGreen,
                unfocusedContainerColor = NightDialColors.SurfaceVariant,
                focusedContainerColor = NightDialColors.SurfaceVariant
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 16.dp),
            singleLine = true
        )

        when {
            uiState.isLoading -> Box(Modifier.fillMaxSize(), Alignment.Center) {
                CircularProgressIndicator(color = NightDialColors.AccentGreen)
            }
            !uiState.permissionGranted -> PermissionPrompt(
                onRequest = { contactsPermissionLauncher.launch(Manifest.permission.READ_CONTACTS) }
            )
            uiState.contacts.isEmpty() -> Box(Modifier.fillMaxSize(), Alignment.Center) {
                Text("No contacts found", color = NightDialColors.OnSurfaceMuted,
                    fontFamily = DMSansFontFamily, fontSize = 14.sp)
            }
            else -> LazyColumn {
                items(uiState.contacts, key = { it.id }) { contact ->
                    ContactItem(contact = contact, onClick = { viewModel.onContactClicked(contact) })
                }
            }
        }
    }
}

@Composable
private fun ContactItem(contact: Contact, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = NightDialColors.AccentGreen.copy(alpha = 0.1f))
            ) { }
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(NightDialColors.SurfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = contact.avatarInitials,
                color = NightDialColors.OnSurface,
                fontSize = 16.sp,
                fontFamily = DMSansFontFamily,
                fontWeight = FontWeight.Medium
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(text = contact.name, color = NightDialColors.OnSurface,
                fontSize = 15.sp, fontFamily = DMSansFontFamily, fontWeight = FontWeight.Medium)
            Text(text = contact.number, color = NightDialColors.OnSurfaceMuted,
                fontSize = 13.sp, fontFamily = DMSansFontFamily, fontWeight = FontWeight.Normal)
        }
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(
                        bounded = true,
                        color = NightDialColors.AccentGreen.copy(alpha = 0.2f)
                    ),
                    onClick = onClick
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.call),
                contentDescription = "Call",
                tint = NightDialColors.AccentGreen,
                modifier = Modifier.size(18.dp)
            )
        }

    }
}

@Composable
private fun PermissionPrompt(onRequest: () -> Unit) {
    Box(Modifier.fillMaxSize(), Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text("Contacts permission required", color = NightDialColors.OnSurfaceMuted,
                fontFamily = DMSansFontFamily)
            Button(onClick = onRequest,
                colors = ButtonDefaults.buttonColors(containerColor = NightDialColors.AccentGreen)) {
                Text("Grant Access", color = Color(0xFF003320), fontFamily = DMSansFontFamily)
            }
        }
    }
}