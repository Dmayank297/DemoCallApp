package com.example.democallapp.ui.screens.dialpad

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class DialPadUiState(
    val inputNumber: String = "",
    val formattedDisplay: String = "",
    val isCallEnabled: Boolean = false
)

@HiltViewModel
class DialPadViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(DialPadUiState())
    val uiState: StateFlow<DialPadUiState> = _uiState.asStateFlow()

    fun onKeyPressed(key: String) {
        _uiState.update { state ->
            val updated = state.inputNumber + key
            state.copy(
                inputNumber = updated,
                formattedDisplay = formatNumber(updated),
                isCallEnabled = updated.isNotEmpty()
            )
        }
    }

    fun onBackspace() {
        _uiState.update { state ->
            val updated = state.inputNumber.dropLast(1)
            state.copy(
                inputNumber = updated,
                formattedDisplay = formatNumber(updated),
                isCallEnabled = updated.isNotEmpty()
            )
        }
    }

    fun onBackspaceLongPress() {
        _uiState.update {
            DialPadUiState()
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