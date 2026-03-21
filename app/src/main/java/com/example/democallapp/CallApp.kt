package com.example.democallapp


import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.democallapp.state.CallState
import com.example.democallapp.ui.screens.CallViewModel
import com.example.democallapp.ui.screens.active.ActiveCallScreen
import com.example.democallapp.ui.screens.active.ActiveCallViewModel
import com.example.democallapp.ui.screens.dialpad.DialPadScreen
import com.example.democallapp.ui.screens.dialpad.DialPadViewModel
import com.example.democallapp.ui.screens.incoming.IncomingCallScreen
import com.example.democallapp.ui.screens.outgoing.OutgoingCallScreen


@Composable
fun CallApp() {
    val appState = rememberAppState()
    val callViewModel: CallViewModel = hiltViewModel()
    val callState by callViewModel.callState.collectAsStateWithLifecycle()

    HandleCallNavigation(
        callState = callState,
        appState = appState
    )

    NavHost(
        navController = appState.navController,
        startDestination = Screen.BottomNavScreen.Keypad.route
    ) {
        nightDialNavGraph(
            appState = appState,
            callViewModel = callViewModel
        )
    }
}

@Composable
fun rememberAppState(
    navController: NavHostController = rememberNavController()
) = remember(navController) {
    NavigationState(navController)
}

@Composable
private fun HandleCallNavigation(
    callState: CallState,
    appState: NavigationState
) {
    val currentRoute = appState.navController.currentBackStackEntry?.destination?.route

    androidx.compose.runtime.LaunchedEffect(callState) {
        when (callState) {
            is CallState.Calling -> {
                if (currentRoute != Screen.OutgoingCall.route) {
                    appState.navigate(Screen.OutgoingCall.route)
                }
            }
            is CallState.Ringing -> {
                if (currentRoute != Screen.IncomingCall.route) {
                    appState.navigate(Screen.IncomingCall.route)
                }
            }
            is CallState.Active -> {
                if (currentRoute != Screen.ActiveCall.route) {
                    appState.navigateAndPopUp(Screen.ActiveCall.route, Screen.IncomingCall.route)
                }
            }
            is CallState.Ended, is CallState.Idle -> {
                val callRoutes = listOf(
                    Screen.OutgoingCall.route,
                    Screen.IncomingCall.route,
                    Screen.ActiveCall.route
                )
                if (currentRoute in callRoutes) {
                    appState.clearAndNavigate(Screen.BottomNavScreen.Keypad.route)
                }
            }
        }
    }
}

fun NavGraphBuilder.nightDialNavGraph(
    appState: NavigationState,
    callViewModel: CallViewModel
) {
    composable(route = Screen.BottomNavScreen.Keypad.route) {
        val dialPadViewModel: DialPadViewModel = hiltViewModel()
        DialPadScreen(
            viewModel = dialPadViewModel,
            onCallInitiated = { number ->
                callViewModel.startOutgoingCall(number)
            },
            onSimulateIncoming = {
                callViewModel.triggerSimulatedIncoming()
            }
        )
    }

    composable(route = Screen.OutgoingCall.route) {
        val callState by callViewModel.callState.collectAsStateWithLifecycle()
        OutgoingCallScreen(
            callState = callState,
            onEndCall = { callViewModel.endCall() }
        )
    }

    composable(route = Screen.IncomingCall.route) {
        val callState by callViewModel.callState.collectAsStateWithLifecycle()
        IncomingCallScreen(
            callState = callState,
            onAccept = { callViewModel.acceptCall() },
            onReject = { callViewModel.rejectCall() }
        )
    }

    composable(route = Screen.ActiveCall.route) {
        val activeCallViewModel: ActiveCallViewModel = hiltViewModel()
        val callState by callViewModel.callState.collectAsStateWithLifecycle()
        ActiveCallScreen(
            callState = callState,
            viewModel = activeCallViewModel,
            onEndCall = { callViewModel.endCall() }
        )
    }
}