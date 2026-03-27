package com.example.democallapp.domain.usecase

import android.content.Intent
import android.net.Uri
import com.example.democallapp.domain.model.CallLogEntry
import com.example.democallapp.domain.model.Contact
import com.example.democallapp.domain.model.RealCallState
import com.example.democallapp.domain.repos.CallLogRepository
import com.example.democallapp.domain.repos.CallStateRepository

import com.example.democallapp.domain.repos.ContactRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetContactsUseCase @Inject constructor(
    private val repository: ContactRepository
) {
    suspend operator fun invoke(query: String = ""): Result<List<Contact>> =
        if (query.isBlank()) repository.getContacts()
        else repository.searchContacts(query)
}

class GetCallLogsUseCase @Inject constructor(
    private val repository: CallLogRepository
) {
    suspend operator fun invoke(): Result<List<CallLogEntry>> =
        repository.getCallLogs()
}

class ObserveCallStateUseCase @Inject constructor(
    private val repository: CallStateRepository
) {
    operator fun invoke(): Flow<RealCallState> =
        repository.observeCallState()
}

class BuildCallIntentUseCase @Inject constructor() {
    operator fun invoke(number: String): Intent =
        Intent(Intent.ACTION_CALL).apply {
            data = Uri.parse("tel:${Uri.encode(number)}")
        }
}