package com.example.democallapp.repos

import com.example.democallapp.model.Contact
import com.example.democallapp.state.CallState
import kotlinx.coroutines.flow.Flow

interface CallRepository {
    fun observeCallState(): Flow<CallState>
    suspend fun resolveContact(number: String): Contact?
    fun getSimulatedIncomingContact(): Contact
}