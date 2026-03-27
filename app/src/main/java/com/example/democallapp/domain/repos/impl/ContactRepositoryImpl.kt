package com.example.democallapp.domain.repos.impl

import com.example.democallapp.data.source.CallLogDataSource
import com.example.democallapp.data.source.ContactsDataSource
import com.example.democallapp.data.source.TelephonyDataSource
import com.example.democallapp.domain.model.CallLogEntry
import com.example.democallapp.domain.model.Contact
import com.example.democallapp.domain.model.RealCallState
import com.example.democallapp.domain.repos.CallLogRepository
import com.example.democallapp.domain.repos.CallStateRepository
import com.example.democallapp.domain.repos.ContactRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactRepositoryImpl @Inject constructor(
    private val dataSource: ContactsDataSource
) : ContactRepository {
    override suspend fun getContacts(): Result<List<Contact>> =
        runCatching { dataSource.getContacts() }

    override suspend fun searchContacts(query: String): Result<List<Contact>> =
        runCatching { dataSource.searchContacts(query) }
}

@Singleton
class CallLogRepositoryImpl @Inject constructor(
    private val dataSource: CallLogDataSource
) : CallLogRepository {
    override suspend fun getCallLogs(): Result<List<CallLogEntry>> =
        runCatching { dataSource.getCallLogs() }
}

@Singleton
class CallStateRepositoryImpl @Inject constructor(
    private val dataSource: TelephonyDataSource
) : CallStateRepository {
    override fun observeCallState(): Flow<RealCallState> =
        dataSource.observeCallState()
}