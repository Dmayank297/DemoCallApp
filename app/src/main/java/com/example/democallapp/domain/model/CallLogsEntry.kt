package com.example.democallapp.domain.model

enum class CallType {
    INCOMING, OUTGOING, MISSED, UNKNOWN
}

data class CallLogEntry(
    val id: Long,
    val number: String,
    val cachedName: String?,
    val type: CallType,
    val dateMs: Long,
    val durationSeconds: Long
) {
    val displayName: String get() = cachedName?.takeIf { it.isNotBlank() } ?: number
    val avatarInitials: String
        get() = displayName.trim().split(" ")
            .mapNotNull { it.firstOrNull()?.uppercaseChar() }
            .take(2)
            .joinToString("")
            .ifEmpty { "#" }
}
