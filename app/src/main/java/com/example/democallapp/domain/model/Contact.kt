package com.example.democallapp.domain.model


data class Contact(
    val id: String,
    val name: String,
    val number: String,
    val photoUri: String? = null
) {
    val avatarInitials: String
        get() = name.trim().split(" ")
            .mapNotNull { it.firstOrNull()?.uppercaseChar() }
            .take(2)
            .joinToString("")
            .ifEmpty { "?" }
}