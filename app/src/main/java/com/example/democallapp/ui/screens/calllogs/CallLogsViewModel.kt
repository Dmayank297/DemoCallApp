package com.example.democallapp.ui.screens.calllogs

import android.content.Intent
import android.text.format.DateUtils
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.democallapp.domain.model.CallLogEntry
import com.example.democallapp.domain.model.CallType
import com.example.democallapp.domain.model.RealCallState
import com.example.democallapp.domain.usecase.BuildCallIntentUseCase
import com.example.democallapp.domain.usecase.GetCallLogsUseCase
import com.example.democallapp.domain.usecase.ObserveCallStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CallLogsUiState(
    val isLoading: Boolean = false,
    val logs: List<CallLogEntry> = emptyList(),
    val error: String? = null,
    val hasCallLogPermission: Boolean = false,
    val hasPhoneStatePermission: Boolean = false
)

@HiltViewModel
class CallLogsViewModel @Inject constructor(
    private val getCallLogsUseCase: GetCallLogsUseCase,
    private val buildCallIntent: BuildCallIntentUseCase,
    private val observeCallState: ObserveCallStateUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CallLogsUiState())
    val uiState: StateFlow<CallLogsUiState> = _uiState.asStateFlow()

    private val _callIntent = MutableSharedFlow<Intent>()
    val callIntent = _callIntent.asSharedFlow()

    private var wasOffhook = false

//    init {
//        observeCallStateForRefresh()
//    }

    private fun observeCallStateForRefresh() {
        observeCallState()
            .onEach { state ->
                when (state) {
                    is RealCallState.Active -> wasOffhook = true
                    is RealCallState.Idle -> {
                        if (wasOffhook) {
                            wasOffhook = false
                            loadCallLogs()
                        }
                    }
                    else -> {}
                }
            }
            .launchIn(viewModelScope)
    }

    fun onCallLogPermissionGranted() {
        _uiState.update { it.copy(hasCallLogPermission = true) }
        onPermissionGranted()
    }

    fun onPhoneStatePermissionGranted() {
        _uiState.update { it.copy(hasPhoneStatePermission = true) }
        onPermissionGranted()
    }
    fun onPermissionGranted() {
        val state = _uiState.value
        if (state.hasCallLogPermission && state.hasPhoneStatePermission) {
            observeCallStateForRefresh()
            loadCallLogs()
        }
    }

    fun onLogClicked(entry: CallLogEntry) {
        viewModelScope.launch {
            _callIntent.emit(buildCallIntent(entry.number))
        }
    }

    fun loadCallLogs() {
        if (!_uiState.value.hasCallLogPermission || !_uiState.value.hasPhoneStatePermission) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            getCallLogsUseCase().fold(
                onSuccess = { logs ->
                    _uiState.update { it.copy(isLoading = false, logs = logs) }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoading = false, error = error.message) }
                }
            )
        }
    }
}

fun Long.toCallDurationString(): String {
    if (this == 0L) return "—"
    val m = this / 60
    val s = this % 60
    return if (m > 0) "${m}m ${s}s" else "${s}s"
}

fun Long.toRelativeDateString(): String =
    DateUtils.getRelativeTimeSpanString(
        this, System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS
    ).toString()

fun CallType.label(): String = when (this) {
    CallType.INCOMING -> "Incoming"
    CallType.OUTGOING -> "Outgoing"
    CallType.MISSED -> "Missed"
    CallType.UNKNOWN -> "Unknown"
}

fun CallType.color() = when (this) {
    CallType.MISSED -> androidx.compose.ui.graphics.Color(0xFFFF3B30)
    CallType.INCOMING -> androidx.compose.ui.graphics.Color(0xFF00E676)
    CallType.OUTGOING -> androidx.compose.ui.graphics.Color(0xFF888899)
    CallType.UNKNOWN -> androidx.compose.ui.graphics.Color(0xFF888899)
}