package com.example.democallapp.data.source

import android.content.ContentResolver
import android.provider.CallLog
import com.example.democallapp.domain.model.CallLogEntry
import com.example.democallapp.domain.model.CallType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CallLogDataSource @Inject constructor(
    private val contentResolver: ContentResolver
) {
    suspend fun getCallLogs(): List<CallLogEntry> = withContext(Dispatchers.IO) {
        val entries = mutableListOf<CallLogEntry>()

        val projection = arrayOf(
            CallLog.Calls._ID,
            CallLog.Calls.NUMBER,
            CallLog.Calls.CACHED_NAME,
            CallLog.Calls.TYPE,
            CallLog.Calls.DATE,
            CallLog.Calls.DURATION
        )

        val cursor = contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            projection,
            null,
            null,
            "${CallLog.Calls.DATE} DESC"
        ) ?: return@withContext entries

        cursor.use {
            val idIdx = it.getColumnIndexOrThrow(CallLog.Calls._ID)
            val numIdx = it.getColumnIndexOrThrow(CallLog.Calls.NUMBER)
            val nameIdx = it.getColumnIndexOrThrow(CallLog.Calls.CACHED_NAME)
            val typeIdx = it.getColumnIndexOrThrow(CallLog.Calls.TYPE)
            val dateIdx = it.getColumnIndexOrThrow(CallLog.Calls.DATE)
            val durIdx = it.getColumnIndexOrThrow(CallLog.Calls.DURATION)

            while (it.moveToNext()) {
                entries.add(
                    CallLogEntry(
                        id = it.getLong(idIdx),
                        number = it.getString(numIdx) ?: "Unknown",
                        cachedName = it.getString(nameIdx)?.takeIf { n -> n.isNotBlank() },
                        type = mapCallType(it.getInt(typeIdx)),
                        dateMs = it.getLong(dateIdx),
                        durationSeconds = it.getLong(durIdx)
                    )
                )
            }
        }
        entries
    }

    private fun mapCallType(type: Int): CallType = when (type) {
        CallLog.Calls.INCOMING_TYPE -> CallType.INCOMING
        CallLog.Calls.OUTGOING_TYPE -> CallType.OUTGOING
        CallLog.Calls.MISSED_TYPE -> CallType.MISSED
        else -> CallType.UNKNOWN
    }
}