package com.example.jagruk.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.jagruk.data.HardcodedData

data class DrawerMenuItem(
    val id: String,
    val title: String,
    val icon: ImageVector,
    val route: String? = null,
    val action: (() -> Unit)? = null
)

@Composable
fun HamburgerMenuDrawer(
    navController: NavHostController,
    onCloseDrawer: () -> Unit
) {
    val drawerMenuItems = getDrawerMenuItems(navController, onCloseDrawer)

    ModalDrawerSheet(
        modifier = Modifier.width(300.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header Section
            DrawerHeader()

            Spacer(modifier = Modifier.height(16.dp))

            Divider()

            Spacer(modifier = Modifier.height(16.dp))

            // Menu Items
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(drawerMenuItems) { item ->
                    DrawerMenuItem(
                        item = item,
                        onClick = {
                            item.action?.invoke() ?: item.route?.let { route ->
                                navController.navigate(route)
                            }
                            onCloseDrawer()
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun DrawerHeader() {
    val user = HardcodedData.sampleUser

    Column {
        Text(
            text = "Jagruk",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = user.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = user.location,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DrawerMenuItem(
    item: DrawerMenuItem,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.title,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = item.title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun getDrawerMenuItems(
    navController: NavHostController,
    onCloseDrawer: () -> Unit
): List<DrawerMenuItem> {
    return listOf(
        DrawerMenuItem(
            id = "profile",
            title = "My Profile",
            icon = Icons.Default.Person,
            route = "profile"
        ),
        DrawerMenuItem(
            id = "achievements",
            title = "Achievements",
            icon = Icons.Default.EmojiEvents,
            route = "achievements"
        ),
        DrawerMenuItem(
            id = "all_modules",
            title = "All Modules",
            icon = Icons.Default.MenuBook,
            route = "all_modules"
        ),
        DrawerMenuItem(
            id = "emergency_kit",
            title = "Emergency Kit Checklist",
            icon = Icons.Default.Inventory,
            route = "emergency_kit"
        ),
        DrawerMenuItem(
            id = "shelters",
            title = "Shelter Locator",
            icon = Icons.Default.Home,
            route = "shelters"
        ),
        DrawerMenuItem(
            id = "contacts",
            title = "Emergency Contacts",
            icon = Icons.Default.ContactPhone,
            route = "contacts"
        ),
        DrawerMenuItem(
            id = "school_safety",
            title = "School Safety Plan",
            icon = Icons.Default.School,
            action = {
                // Placeholder for school safety plan
            }
        ),
        DrawerMenuItem(
            id = "drill_schedule",
            title = "Drill Schedule",
            icon = Icons.Default.Schedule,
            action = {
                // Placeholder for drill schedule
            }
        ),
        DrawerMenuItem(
            id = "resources",
            title = "Resources & FAQs",
            icon = Icons.Default.Help,
            action = {
                // Placeholder for resources
            }
        ),
        DrawerMenuItem(
            id = "settings",
            title = "Settings",
            icon = Icons.Default.Settings,
            route = "settings"
        ),
        DrawerMenuItem(
            id = "about",
            title = "About Us / Help",
            icon = Icons.Default.Info,
            action = {
                // Placeholder for about/help
            }
        ),
        DrawerMenuItem(
            id = "logout",
            title = "Logout",
            icon = Icons.Default.Logout,
            action = {
                // Handle logout
            }
        )
    )
}