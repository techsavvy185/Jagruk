package com.example.jagruk.ui.screens.contacts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.jagruk.data.models.EmergencyContact
import com.example.jagruk.ui.theme.JagrukTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmergencyContactsScreen(
    viewModel: ContactsViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val uiState by viewModel.contactsUiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Emergency Contacts") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.showAddContactDialog() }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Contact")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* Call emergency services */ },
                containerColor = Color(0xFFE53E3E)
            ) {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = "Emergency Call",
                    tint = Color.White
                )
            }
        }
    ) { paddingValues ->
        EmergencyContactsLayout(
            uiState = uiState,
            onContactCall = viewModel::callContact,
            onContactEdit = viewModel::editContact,
            onContactDelete = viewModel::deleteContact,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
private fun EmergencyContactsLayout(
    uiState: ContactsUiState,
    onContactCall: (String) -> Unit,
    onContactEdit: (String) -> Unit,
    onContactDelete: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            SectionHeader(title = "Emergency Services")
        }

        val emergencyServices = uiState.contacts.filter {
            it.relationship == "Emergency Service" || it.relationship == "Medical Emergency"
        }

        items(emergencyServices) { contact ->
            EmergencyServiceCard(
                contact = contact,
                onCallClick = { onContactCall(contact.phoneNumber) }
            )
        }

        // Personal Contacts Section
        item {
            Spacer(modifier = Modifier.height(8.dp))
            SectionHeader(title = "Personal Contacts")
        }

        val personalContacts = uiState.contacts.filter {
            it.relationship != "Emergency Service" && it.relationship != "Medical Emergency"
        }

        items(personalContacts) { contact ->
            PersonalContactCard(
                contact = contact,
                onCallClick = { onContactCall(contact.phoneNumber) },
                onEditClick = { onContactEdit(contact.id) },
                onDeleteClick = { onContactDelete(contact.id) }
            )
        }

        if (personalContacts.isEmpty()) {
            item {
                EmptyContactsView()
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
private fun EmergencyServiceCard(
    contact: EmergencyContact,
    onCallClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = Color(0xFFE53E3E),
                shape = RoundedCornerShape(50),
                modifier = Modifier.size(48.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = getContactIcon(contact.relationship),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = contact.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = contact.phoneNumber,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black.copy(alpha = 0.8f),
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = contact.relationship,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Black.copy(alpha = 0.6f),
                    )
            }

            Button(
                onClick = onCallClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE53E3E)
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Call", color = Color.White)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PersonalContactCard(
    contact: EmergencyContact,
    onCallClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(50),
                modifier = Modifier.size(48.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = contact.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    if (contact.isPrimary) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Primary",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Text(
                    text = contact.phoneNumber,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )

                Text(
                    text = contact.relationship,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }

            Row {
                IconButton(onClick = onCallClick) {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = "Call",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                IconButton(onClick = onEditClick) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }

                IconButton(onClick = onDeleteClick) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyContactsView() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.ContactPhone,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "No Personal Contacts",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Add emergency contacts for your family and friends. Tap the + button to get started.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}

private fun getContactIcon(relationship: String) = when {
    relationship.contains("Police", ignoreCase = true) -> Icons.Default.LocalPolice
    relationship.contains("Fire", ignoreCase = true) -> Icons.Default.FireTruck
    relationship.contains("Medical", ignoreCase = true) -> Icons.Default.LocalHospital
    relationship.contains("Emergency", ignoreCase = true) -> Icons.Default.Emergency
    else -> Icons.Default.Phone
}

@Preview(showBackground = true)
@Composable
fun EmergencyContactsScreenPreview() {
    JagrukTheme {
        // Preview implementation would go here
    }
}