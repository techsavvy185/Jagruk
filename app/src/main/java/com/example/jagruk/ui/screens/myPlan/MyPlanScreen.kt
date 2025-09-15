package com.example.jagruk.ui.screens.myPlan

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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.jagruk.data.models.EmergencyContact
import com.example.jagruk.data.models.EmergencyKit
import com.example.jagruk.data.models.EmergencyKitItem
import com.example.jagruk.data.models.FamilyPlan
import com.example.jagruk.ui.theme.JagrukTheme

@Composable
fun MyPlanScreen(
    viewModel: MyPlanViewModel = hiltViewModel(),
    onNavigateToEmergencyKit: () -> Unit = {},
    onNavigateToContacts: () -> Unit = {},
    onNavigateToShelters: () -> Unit = {}
) {
    val uiState by viewModel.myPlanUiState.collectAsState()

    MyPlanScreenLayout(
        uiState = uiState,
        onKitItemToggle = viewModel::toggleKitItem,
        onNavigateToEmergencyKit = onNavigateToEmergencyKit,
        onNavigateToContacts = onNavigateToContacts,
        onNavigateToShelters = onNavigateToShelters,
        onAddContact = viewModel::addEmergencyContact,
        onUpdatePlan = viewModel::updateFamilyPlan
    )
}

@Composable
private fun MyPlanScreenLayout(
    uiState: MyPlanUiState,
    onKitItemToggle: (String) -> Unit,
    onNavigateToEmergencyKit: () -> Unit,
    onNavigateToContacts: () -> Unit,
    onNavigateToShelters: () -> Unit,
    onAddContact: (EmergencyContact) -> Unit,
    onUpdatePlan: (FamilyPlan) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            PlanOverviewCard(
                completedItems = uiState.completedPreparednessItems,
                totalItems = uiState.totalPreparednessItems
            )
        }

        item {
            EmergencyKitCard(
                emergencyKit = uiState.emergencyKit,
                onKitItemToggle = onKitItemToggle,
                onNavigateToFullKit = onNavigateToEmergencyKit
            )
        }

        item {
            FamilyPlanCard(
                familyPlan = uiState.familyPlan,
                onNavigateToContacts = onNavigateToContacts,
                onNavigateToShelters = onNavigateToShelters
            )
        }

        item {
            ImportantDocumentsCard()
        }

        item {
            CommunicationPlanCard(
                emergencyContacts = uiState.emergencyContacts.take(3)
            )
        }
    }
}

@Composable
private fun PlanOverviewCard(
    completedItems: Int,
    totalItems: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Emergency Preparedness",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            val progress = if (totalItems > 0) completedItems.toFloat() / totalItems.toFloat() else 0f

            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "$completedItems of $totalItems items completed",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            val completionPercentage = (progress * 100).toInt()
            Text(
                text = "$completionPercentage% ready for emergencies",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EmergencyKitCard(
    emergencyKit: EmergencyKit?,
    onKitItemToggle: (String) -> Unit,
    onNavigateToFullKit: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Emergency Kit",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                TextButton(onClick = onNavigateToFullKit) {
                    Text("View All")
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            emergencyKit?.let { kit ->
                val priorityItems = kit.items.filter { it.isPriority }.take(4)
                val checkedCount = priorityItems.count { it.isChecked }

                Text(
                    text = "Priority Items ($checkedCount/${priorityItems.size})",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )

                Spacer(modifier = Modifier.height(8.dp))

                priorityItems.forEach { item ->
                    KitItemRow(
                        item = item,
                        onToggle = { onKitItemToggle(item.id) }
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }

                if (kit.items.size > priorityItems.size) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "+${kit.items.size - priorityItems.size} more items",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            }
        }
    }
}

@Composable
private fun KitItemRow(
    item: EmergencyKitItem,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Checkbox(
                checked = item.isChecked,
                onCheckedChange = { onToggle() }
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = item.name,
                style = MaterialTheme.typography.bodyMedium,
                color = if (item.isChecked) {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                } else {
                    MaterialTheme.colorScheme.onSurface
                }
            )
        }

        if (item.isPriority && !item.isChecked) {
            Surface(
                color = MaterialTheme.colorScheme.error,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Priority",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onError,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FamilyPlanCard(
    familyPlan: FamilyPlan?,
    onNavigateToContacts: () -> Unit,
    onNavigateToShelters: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "Family Emergency Plan",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                PlanActionButton(
                    icon = Icons.Default.Contacts,
                    text = "Emergency\nContacts",
                    onClick = onNavigateToContacts
                )

                PlanActionButton(
                    icon = Icons.Default.LocationOn,
                    text = "Meeting\nPoints",
                    onClick = { /* Navigate to meeting points */ }
                )

                PlanActionButton(
                    icon = Icons.Default.Home,
                    text = "Find\nShelters",
                    onClick = onNavigateToShelters
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            familyPlan?.let { plan ->
                Text(
                    text = "Plan Summary:",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "• ${plan.emergencyContacts.size} emergency contacts saved",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )

                Text(
                    text = "• ${plan.meetingPoints.size} meeting points identified",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )

                Text(
                    text = "• ${plan.importantDocuments.size} important documents listed",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlanActionButton(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        modifier = Modifier.width(100.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = text,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ImportantDocumentsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Description,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Important Documents",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Keep copies of these documents in a waterproof container:",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            val documents = listOf(
                "Government ID (Aadhaar, PAN)",
                "Insurance policies",
                "Bank account records",
                "Medical records",
                "Property documents"
            )

            documents.forEach { document ->
                Row(
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier.padding(bottom = 4.dp)
                ) {
                    Text(
                        text = "• ",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Text(
                        text = document,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}

@Composable
private fun CommunicationPlanCard(
    emergencyContacts: List<EmergencyContact>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ContactPhone,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Communication Plan",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Key Emergency Contacts:",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (emergencyContacts.isNotEmpty()) {
                emergencyContacts.forEach { contact ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = contact.name,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                        Text(
                            text = contact.phoneNumber,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.8f)
                        )
                    }
                }
            } else {
                Text(
                    text = "No emergency contacts added yet.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MyPlanScreenPreview() {
    JagrukTheme {
        // Preview implementation would go here
    }
}