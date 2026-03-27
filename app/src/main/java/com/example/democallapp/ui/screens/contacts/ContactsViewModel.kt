package com.example.democallapp.ui.screens.contacts

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.democallapp.domain.model.Contact
import com.example.democallapp.domain.usecase.BuildCallIntentUseCase
import com.example.democallapp.domain.usecase.GetContactsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ContactsUiState(
    val isLoading: Boolean = false,
    val contacts: List<Contact> = emptyList(),
    val searchQuery: String = "",
    val error: String? = null,
    val permissionGranted: Boolean = false
)

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val getContactsUseCase: GetContactsUseCase,
    private val buildCallIntent: BuildCallIntentUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ContactsUiState())
    val uiState: StateFlow<ContactsUiState> = _uiState.asStateFlow()

    private val _callIntent = MutableSharedFlow<Intent>()
    val callIntent = _callIntent.asSharedFlow()

    private val _searchQuery = MutableStateFlow("")

    init {
        observeSearch()
    }

    @OptIn(FlowPreview::class)
    private fun observeSearch() {
        _searchQuery
            .debounce(300)
            .distinctUntilChanged()
            .onEach { query ->
                loadContacts(query)
            }
            .launchIn(viewModelScope)
    }

    fun onPermissionGranted() {
        _uiState.update { it.copy(permissionGranted = true) }
        loadContacts()
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        _searchQuery.value = query
    }

    fun onContactClicked(contact: Contact) {
        viewModelScope.launch {
            _callIntent.emit(buildCallIntent(contact.number))
        }
    }

    fun loadContacts(query: String = _searchQuery.value) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            getContactsUseCase(query).fold(
                onSuccess = { contacts ->
                    _uiState.update { it.copy(isLoading = false, contacts = contacts) }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoading = false, error = error.message) }
                }
            )
        }
    }
}