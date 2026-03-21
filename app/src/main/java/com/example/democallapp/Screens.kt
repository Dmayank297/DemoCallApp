package com.example.democallapp

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Stable

@Stable
sealed class Screen(val title: String, val route: String) {

    sealed class BottomNavScreen(
        val bTitle: String,
        val bRoute: String,
        @DrawableRes val bIcon: Int = 0,
        @DrawableRes val bSelectedIcon: Int = 0,
    ) : Screen(bTitle, bRoute) {

        object Recents : BottomNavScreen(
            bTitle = "Recents",
            bRoute = "RecentsScreen",
            bIcon = R.drawable.recents,
            bSelectedIcon = R.drawable.recents_filled
        )

        object Keypad : BottomNavScreen(
            bTitle = "Keypad",
            bRoute = "DialPadScreen",
            bIcon = R.drawable.dialpad,
            bSelectedIcon = R.drawable.dialpad
        )

        object Contacts : BottomNavScreen(
            bTitle = "Contacts",
            bRoute = "ContactsScreen",
            bIcon = R.drawable.contacts,
            bSelectedIcon = R.drawable.contacts_filled
        )

        object Settings : BottomNavScreen(
            bTitle = "Settings",
            bRoute = "SettingsScreen",
            bIcon = R.drawable.settings,
            bSelectedIcon = R.drawable.settings_filled
        )
    }

    object OutgoingCall : Screen("Outgoing Call", "OutgoingCallScreen")
    object IncomingCall : Screen("Incoming Call", "IncomingCallScreen")
    object ActiveCall : Screen("Active Call", "ActiveCallScreen")
}

val bottomNavItems = listOf(
    Screen.BottomNavScreen.Recents,
    Screen.BottomNavScreen.Keypad,
    Screen.BottomNavScreen.Contacts,
    Screen.BottomNavScreen.Settings
)