package com.example.democallapp.state

import com.example.democallapp.model.Contact


sealed class CallState {
    object Idle : CallState()
    data class Calling(val number: String) : CallState()
    data class Ringing(val contact: Contact) : CallState()
    data class Active(val contact: Contact, val elapsedSeconds: Int = 0) : CallState()
    object Ended : CallState()
}