package com.example.democallapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.democallapp.ui.screens.active.ActiveCallScreen
import com.example.democallapp.ui.screens.active.ActiveCallViewModel
import com.example.democallapp.ui.screens.calllogs.CallLogsScreen
import com.example.democallapp.ui.screens.calllogs.CallLogsViewModel
import com.example.democallapp.ui.screens.contacts.ContactsScreen
import com.example.democallapp.ui.screens.contacts.ContactsViewModel
import com.example.democallapp.ui.screens.dialpad.DialPadScreen
import com.example.democallapp.ui.screens.dialpad.DialPadViewModel
import com.example.democallapp.ui.theme.DMSansFontFamily
import com.example.democallapp.ui.theme.NightDialColors

@Composable
fun CallApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val isOnCallScreen = currentDestination?.route?.startsWith("ActiveCallScreen") == true

    Column(modifier = Modifier.fillMaxSize().background(NightDialColors.Background)) {
        Box(modifier = Modifier.weight(1f)) {
            NavHost(
                navController = navController,
                startDestination = Screen.BottomNavScreen.Keypad.route
            ) {
                composable(Screen.BottomNavScreen.Keypad.route) {
                    val vm: DialPadViewModel = hiltViewModel()
                    DialPadScreen(viewModel = vm)
                }
                composable(Screen.BottomNavScreen.Recents.route) {
                    val vm: CallLogsViewModel = hiltViewModel()
                    CallLogsScreen(viewModel = vm)
                }
                composable(Screen.BottomNavScreen.Contacts.route) {
                    val vm: ContactsViewModel = hiltViewModel()
                    ContactsScreen(viewModel = vm)
                }
                composable(Screen.ActiveCall.route + "/{name}/{number}") { backStack ->
                    val vm: ActiveCallViewModel = hiltViewModel()
                    val name = backStack.arguments?.getString("name") ?: ""
                    val number = backStack.arguments?.getString("number") ?: ""
                    ActiveCallScreen(
                        callerName = name,
                        callerNumber = number,
                        viewModel = vm
                    )
                }
            }
        }

        if (!isOnCallScreen) {
            CallAppBottomBar(
                currentDestination = currentDestination,
                onNavigate = { screen ->
                    navController.navigate(screen.bRoute) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
private fun CallAppBottomBar(
    currentDestination: androidx.navigation.NavDestination?,
    onNavigate: (Screen.BottomNavScreen) -> Unit
) {
    NavigationBar(
        modifier = Modifier
            .fillMaxWidth(),
        containerColor = NightDialColors.Surface,
        tonalElevation = 0.dp
    ) {
        bottomNavItems.forEach { screen ->
            val selected = currentDestination?.hierarchy?.any { it.route == screen.bRoute } == true
            NavigationBarItem(
                selected = selected,
                onClick = { onNavigate(screen) },
                icon = {
                    Icon(
                        painter = painterResource(
                            if (selected) screen.bSelectedIcon else screen.bIcon
                        ),
                        contentDescription = screen.bTitle,
                        modifier = Modifier.size(22.dp)
                    )
                },
                label = {
                    Text(
                        text = screen.bTitle,
                        fontSize = 11.sp,
                        fontFamily = DMSansFontFamily,
                        fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = NightDialColors.AccentGreen,
                    selectedTextColor = NightDialColors.AccentGreen,
                    unselectedIconColor = NightDialColors.OnSurfaceMuted,
                    unselectedTextColor = NightDialColors.OnSurfaceMuted,
                    indicatorColor = NightDialColors.SurfaceVariant
                )
            )
        }
    }
}