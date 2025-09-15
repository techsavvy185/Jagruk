package com.example.jagruk.ui.screens.alerts

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.jagruk.data.models.Alert
import com.example.jagruk.data.models.AlertSeverity
import com.example.jagruk.data.models.AlertType
import com.example.jagruk.ui.theme.JagrukTheme
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AlertsScreen(
    viewModel: AlertsViewModel = hiltViewModel()
) {
    val uiState by viewModel.alertsUiState.collectAsState()

    AlertsScreenLayout(
        uiState = uiState,
        onRefresh = viewModel::refreshAlerts,
        onAlertClick = viewModel::markAlertAsRead
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AlertsScreenLayout(
    uiState: AlertsUiState,
    onRefresh: () -> Unit,
    onAlertClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header with refresh
        Surface(
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Emergency Alerts",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "Stay informed about local hazards",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                    )
                }

                IconButton(onClick = onRefresh) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh alerts",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }

        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Active Alerts Section
                val activeAlerts = uiState.alerts.filter { it.isActive }

                if (activeAlerts.isNotEmpty()) {
                    item {
                        Text(
                            text = "Active Alerts (${activeAlerts.size})",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                    items(activeAlerts) { alert ->
                        AlertCard(
                            alert = alert,
                            onClick = { onAlertClick(alert.id) }
                        )
                    }
                }

                // Recent Alerts Section
                val recentAlerts = uiState.alerts.filter { !it.isActive }

                if (recentAlerts.isNotEmpty()) {
                    item {
                        Text(
                            text = "Recent Alerts",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                    items(recentAlerts) { alert ->
                        AlertCard(
                            alert = alert,
                            onClick = { onAlertClick(alert.id) }
                        )
                    }
                }

                if (uiState.alerts.isEmpty()) {
                    item {
                        EmptyAlertsView()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AlertCard(
    alert: Alert,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = getAlertCardColor(alert.severity, alert.isActive)
        ),
        border = if (alert.isActive) CardDefaults.outlinedCardBorder() else null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = getAlertIcon(alert.type),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = getAlertIconColor(alert.severity)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Column {
                        Text(
                            text = alert.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = alert.location,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }

                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    SeverityChip(severity = alert.severity)

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = formatTimestamp(alert.timestamp),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = alert.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )

            if (alert.actionItems.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Recommended Actions:",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(8.dp))

                alert.actionItems.forEach { actionItem ->
                    Row(
                        verticalAlignment = Alignment.Top,
                        modifier = Modifier.padding(bottom = 4.dp)
                    ) {
                        Text(
                            text = "• ",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = actionItem,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            if (alert.isActive) {
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Circle,
                        contentDescription = null,
                        modifier = Modifier.size(8.dp),
                        tint = Color.Red
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Active Alert",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Red,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun SeverityChip(
    severity: AlertSeverity
) {
    val (backgroundColor, textColor, text) = when (severity) {
        AlertSeverity.LOW -> Triple(
            Color(0xFF4CAF50),
            Color.White,
            "LOW"
        )
        AlertSeverity.MODERATE -> Triple(
            Color(0xFFFF9800),
            Color.White,
            "MODERATE"
        )
        AlertSeverity.HIGH -> Triple(
            Color(0xFFFF5722),
            Color.White,
            "HIGH"
        )
        AlertSeverity.CRITICAL -> Triple(
            Color(0xFFD32F2F),
            Color.White,
            "CRITICAL"
        )
    }

    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.clip(RoundedCornerShape(12.dp))
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = textColor,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun EmptyAlertsView() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "All Clear!",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "No active alerts in your area. Stay prepared and check back regularly.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}

private fun getAlertCardColor(severity: AlertSeverity, isActive: Boolean): Color {
    return if (isActive) {
        when (severity) {
            AlertSeverity.CRITICAL -> Color(0xFFFFEBEE)
            AlertSeverity.HIGH -> Color(0xFFFFF3E0)
            AlertSeverity.MODERATE -> Color(0xFFFFF8E1)
            AlertSeverity.LOW -> Color(0xFFF1F8E9)
        }
    } else {
        Color.Transparent
    }
}

private fun getAlertIcon(type: AlertType): ImageVector {
    return when (type) {
        AlertType.EARTHQUAKE -> Icons.Default.Warning
        AlertType.LANDSLIDE -> Icons.Default.Landscape
        AlertType.FLOOD -> Icons.Default.Water
        AlertType.CYCLONE -> Icons.Default.Cyclone
        AlertType.HEAT_WAVE -> Icons.Default.WbSunny
        AlertType.GENERAL -> Icons.Default.Info
    }
}

private fun getAlertIconColor(severity: AlertSeverity): Color {
    return when (severity) {
        AlertSeverity.CRITICAL -> Color(0xFFD32F2F)
        AlertSeverity.HIGH -> Color(0xFFFF5722)
        AlertSeverity.MODERATE -> Color(0xFFFF9800)
        AlertSeverity.LOW -> Color(0xFF4CAF50)
    }
}

private fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

@Preview(showBackground = true)
@Composable
fun AlertsScreenPreview() {
    JagrukTheme {
        // Preview implementation would go here
    }
}