package com.example.democallapp.data.source

import android.content.ContentResolver
import android.provider.ContactsContract
import com.example.democallapp.domain.model.Contact
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactsDataSource @Inject constructor(
    private val contentResolver: ContentResolver
) {
    suspend fun getContacts(): List<Contact> = withContext(Dispatchers.IO) {
        val contacts = mutableListOf<Contact>()
        val seenNumbers = mutableSetOf<String>()

        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone._ID,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI
        )

        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            projection,
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        ) ?: return@withContext contacts

        cursor.use {
            val idIdx = it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone._ID)
            val nameIdx = it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val numIdx = it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER)
            val photoIdx = it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI)

            while (it.moveToNext()) {
                val rawNumber = it.getString(numIdx) ?: continue
                val normalized = rawNumber.replace(Regex("[^0-9+]"), "")
                if (normalized.isBlank() || !seenNumbers.add(normalized)) continue

                contacts.add(
                    Contact(
                        id = it.getLong(idIdx).toString(),
                        name = it.getString(nameIdx) ?: "Unknown",
                        number = rawNumber,
                        photoUri = it.getString(photoIdx)
                    )
                )
            }
        }
        contacts
    }

    suspend fun searchContacts(query: String): List<Contact> = withContext(Dispatchers.IO) {
        val all = getContacts()
        all.filter {
            it.name.contains(query, ignoreCase = true) ||
                    it.number.contains(query)
        }
    }
}