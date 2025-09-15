package com.example.jagruk.ui.screens.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jagruk.data.HardcodedData
import com.example.jagruk.data.models.EmergencyContact
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.filter

@HiltViewModel
class ContactsViewModel @Inject constructor(
    // In a real app, you'd inject repositories here
) : ViewModel() {

    private val _contactsUiState = MutableStateFlow(ContactsUiState())
    val contactsUiState: StateFlow<ContactsUiState> = _contactsUiState.asStateFlow()

    init {
        loadContacts()
    }

    fun callContact(phoneNumber: String) {
        // In a real app, this would trigger the phone dialer
        viewModelScope.launch {
            // Log the call attempt or handle phone intent
        }
    }

    fun editContact(contactId: String) {
        _contactsUiState.update { currentState ->
            currentState.copy(editingContactId = contactId)
        }
    }

    fun deleteContact(contactId: String) {
        _contactsUiState.update { currentState ->
            val updatedContacts = currentState.contacts.filter { it.id != contactId }
            currentState.copy(contacts = updatedContacts)
        }
    }

    fun showAddContactDialog() {
        _contactsUiState.update { currentState ->
            currentState.copy(showAddContactDialog = true)
        }
    }

    fun hideAddContactDialog() {
        _contactsUiState.update { currentState ->
            currentState.copy(showAddContactDialog = false)
        }
    }

    fun addContact(contact: EmergencyContact) {
        _contactsUiState.update { currentState ->
            val updatedContacts = currentState.contacts + contact
            currentState.copy(
                contacts = updatedContacts,
                showAddContactDialog = false
            )
        }
    }

    private fun loadContacts() {
        viewModelScope.launch {
            _contactsUiState.update {
                it.copy(
                    isLoading = false,
                    contacts = HardcodedData.emergencyContacts
                )
            }
        }
    }
}

data class ContactsUiState(
    val isLoading: Boolean = true,
    val contacts: List<EmergencyContact> = emptyList(),
    val editingContactId: String? = null,
    val showAddContactDialog: Boolean = false,
    val error: String? = null
)