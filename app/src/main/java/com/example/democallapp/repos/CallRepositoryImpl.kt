package com.example.democallapp.repos

import com.example.democallapp.model.Contact
import com.example.democallapp.state.CallState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CallRepositoryImpl @Inject constructor() : CallRepository {

    private val _callState = MutableStateFlow<CallState>(CallState.Idle)

    private val contactDirectory = mapOf(
        "1234567890" to Contact("1", "Sarah Wilson", "1234567890"),
        "9876543210" to Contact("2", "James Carter", "9876543210"),
        "5551234567" to Contact("3", "Mia Thompson", "5551234567"),
    )

    override fun observeCallState(): Flow<CallState> = _callState

    override suspend fun resolveContact(number: String): Contact? =
        contactDirectory[number.filter { it.isDigit() }]

    override fun getSimulatedIncomingContact(): Contact =
        Contact("sim_1", "Sarah Wilson", "+1 (555) 867-5309")
}