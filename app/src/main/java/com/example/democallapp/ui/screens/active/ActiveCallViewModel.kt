package com.example.democallapp.ui.screens.active

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class ActiveCallUiState(
    val isMuted: Boolean = false,
    val isSpeakerOn: Boolean = false,
    val isKeypadVisible: Boolean = false
)

@HiltViewModel
class ActiveCallViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(ActiveCallUiState())
    val uiState: StateFlow<ActiveCallUiState> = _uiState.asStateFlow()

    fun toggleMute() {
        _uiState.update { it.copy(isMuted = !it.isMuted) }
    }

    fun toggleSpeaker() {
        _uiState.update { it.copy(isSpeakerOn = !it.isSpeakerOn) }
    }

    fun toggleKeypad() {
        _uiState.update { it.copy(isKeypadVisible = !it.isKeypadVisible) }
    }
}