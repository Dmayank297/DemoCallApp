package com.example.democallapp.domain.repos

import com.example.democallapp.domain.model.Contact
import com.example.democallapp.domain.model.CallLogEntry
import com.example.democallapp.domain.model.RealCallState
import kotlinx.coroutines.flow.Flow

interface ContactRepository {
    suspend fun getContacts(): Result<List<Contact>>
    suspend fun searchContacts(query: String): Result<List<Contact>>
}

interface CallLogRepository {
    suspend fun getCallLogs(): Result<List<CallLogEntry>>
}

interface CallStateRepository {
    fun observeCallState(): Flow<RealCallState>
}