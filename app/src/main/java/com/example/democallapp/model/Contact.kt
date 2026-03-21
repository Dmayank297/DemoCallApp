package com.example.democallapp.model


data class Contact(
    val id: String,
    val name: String,
    val number: String,
    val avatarInitials: String = name.take(2).uppercase()
)