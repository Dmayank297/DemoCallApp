package com.example.democallapp.domain.model


sealed class RealCallState {
    object Idle : RealCallState()
    object Ringing : RealCallState()
    object Active : RealCallState()
}