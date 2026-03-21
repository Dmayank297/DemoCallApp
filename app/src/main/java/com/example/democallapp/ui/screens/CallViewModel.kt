package com.example.democallapp.ui.screens


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.democallapp.repos.CallRepository
import com.example.democallapp.state.CallState

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CallViewModel @Inject constructor(
    private val repository: CallRepository
) : ViewModel() {

    private val _callState = MutableStateFlow<CallState>(CallState.Idle)
    val callState: StateFlow<CallState> = _callState.asStateFlow()

    private var timerJob: Job? = null
    private var simulatedIncomingJob: Job? = null

    fun startOutgoingCall(number: String) {
        viewModelScope.launch {
            _callState.value = CallState.Calling(number)
            simulateIncomingAfterDelay()
        }
    }

    fun endCall() {
        timerJob?.cancel()
        simulatedIncomingJob?.cancel()
        _callState.value = CallState.Ended
        viewModelScope.launch {
            delay(1200)
            _callState.value = CallState.Idle
        }
    }

    fun acceptCall() {
        val current = _callState.value
        if (current is CallState.Ringing) {
            _callState.value = CallState.Active(current.contact)
            startCallTimer()
        }
    }

    fun rejectCall() {
        simulatedIncomingJob?.cancel()
        _callState.value = CallState.Ended
        viewModelScope.launch {
            delay(800)
            _callState.value = CallState.Idle
        }
    }

    fun triggerSimulatedIncoming() {
        simulatedIncomingJob?.cancel()
        val contact = repository.getSimulatedIncomingContact()
        _callState.value = CallState.Ringing(contact)
    }

    private fun simulateIncomingAfterDelay() {
        simulatedIncomingJob = viewModelScope.launch {
            delay(3000)
            val contact = repository.getSimulatedIncomingContact()
            _callState.value = CallState.Ringing(contact)
        }
    }

    private fun startCallTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                val current = _callState.value
                if (current is CallState.Active) {
                    _callState.update { CallState.Active(current.contact, current.elapsedSeconds + 1) }
                } else break
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        simulatedIncomingJob?.cancel()
    }
}