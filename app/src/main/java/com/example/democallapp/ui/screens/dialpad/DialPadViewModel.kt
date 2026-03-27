package com.example.democallapp.ui.screens.dialpad

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.democallapp.domain.usecase.BuildCallIntentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DialPadUiState(
    val inputNumber: String = "",
    val formattedDisplay: String = "",
    val isCallEnabled: Boolean = false
)

sealed class DialPadEvent {
    data class KeyPressed(val key: String) : DialPadEvent()
    object Backspace : DialPadEvent()
    object BackspaceLongPress : DialPadEvent()
    object CallPressed : DialPadEvent()
}

@HiltViewModel
class DialPadViewModel @Inject constructor(
    private val buildCallIntent: BuildCallIntentUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DialPadUiState())
    val uiState: StateFlow<DialPadUiState> = _uiState.asStateFlow()

    private val _callIntent = MutableSharedFlow<Intent>()
    val callIntent = _callIntent.asSharedFlow()

    fun onEvent(event: DialPadEvent) {
        when (event) {
            is DialPadEvent.KeyPressed -> appendKey(event.key)
            DialPadEvent.Backspace -> backspace()
            DialPadEvent.BackspaceLongPress -> clearAll()
            DialPadEvent.CallPressed -> initiateCall()
        }
    }

    private fun appendKey(key: String) {
        _uiState.update { state ->
            val updated = state.inputNumber + key
            state.copy(
                inputNumber = updated,
                formattedDisplay = formatNumber(updated),
                isCallEnabled = updated.isNotEmpty()
            )
        }
    }

    private fun backspace() {
        _uiState.update { state ->
            val updated = state.inputNumber.dropLast(1)
            state.copy(
                inputNumber = updated,
                formattedDisplay = formatNumber(updated),
                isCallEnabled = updated.isNotEmpty()
            )
        }
    }

    private fun clearAll() {
        _uiState.update { DialPadUiState() }
    }

    private fun initiateCall() {
        val number = _uiState.value.inputNumber
        if (number.isBlank()) return
        viewModelScope.launch {
            _callIntent.emit(buildCallIntent(number))
        }
    }

    private fun formatNumber(raw: String): String {
        val digits = raw.filter { it.isDigit() }
        return when {
            digits.length <= 3 -> digits
            digits.length <= 6 -> "${digits.take(3)} ${digits.drop(3)}"
            digits.length <= 10 -> "${digits.take(3)} ${digits.drop(3).take(3)} ${digits.drop(6)}"
            else -> raw
        }
    }
}