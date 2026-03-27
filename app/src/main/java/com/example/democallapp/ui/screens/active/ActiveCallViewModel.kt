package com.example.democallapp.ui.screens.active

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.democallapp.domain.model.RealCallState
import com.example.democallapp.domain.usecase.ObserveCallStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ActiveCallUiState(
    val isCallActive: Boolean = false,
    val isMuted: Boolean = false,
    val isSpeakerOn: Boolean = false,
    val elapsedSeconds: Int = 0
)

@HiltViewModel
class ActiveCallViewModel @Inject constructor(
    private val observeCallState: ObserveCallStateUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ActiveCallUiState())
    val uiState: StateFlow<ActiveCallUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    init {
        observeCallState()
            .onEach { state ->
                when (state) {
                    RealCallState.Active -> {
                        _uiState.update { it.copy(isCallActive = true) }
                        startTimer()
                    }
                    RealCallState.Idle -> {
                        _uiState.update { it.copy(isCallActive = false) }
                        stopTimer()
                    }
                    else -> {}
                }
            }
            .launchIn(viewModelScope)
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                _uiState.update { it.copy(elapsedSeconds = it.elapsedSeconds + 1) }
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        _uiState.update { it.copy(elapsedSeconds = 0) }
    }

    fun toggleMute() = _uiState.update { it.copy(isMuted = !it.isMuted) }
    fun toggleSpeaker() = _uiState.update { it.copy(isSpeakerOn = !it.isSpeakerOn) }
}