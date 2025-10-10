package upc.edu.pe.levelupjourney.presentation.screen.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onSignOut: () -> Unit,
    onProfileClick: () -> Unit = {}
) {
    var showSignOutDialog by remember { mutableStateOf(false) }
    var showDeleteAccountDialog by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Settings",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }
            
            // Account Section
            item {
                SectionHeader(
                    title = "Account",
                    icon = Icons.Outlined.Person
                )
            }
            
            item {
                SettingsCard {
                    SettingsItem(
                        title = "Profile",
                        description = "View and edit your profile",
                        icon = Icons.Outlined.AccountCircle,
                        onClick = onProfileClick
                    )
                    HorizontalDivider()
                    SettingsItem(
                        title = "Privacy",
                        description = "Control who can see your information",
                        icon = Icons.Outlined.Lock,
                        onClick = { /* TODO */ }
                    )
                    HorizontalDivider()
                    SettingsItem(
                        title = "Security",
                        description = "Password and authentication",
                        icon = Icons.Outlined.Security,
                        onClick = { /* TODO */ }
                    )
                }
            }
            
            // Notifications Section
            item {
                SectionHeader(
                    title = "Notifications",
                    icon = Icons.Outlined.Notifications
                )
            }
            
            item {
                SettingsCard {
                    SettingsItem(
                        title = "Push Notifications",
                        description = "Receive notifications about your activities",
                        icon = Icons.Outlined.NotificationsActive,
                        onClick = { /* TODO */ }
                    )
                    HorizontalDivider()
                    SettingsItem(
                        title = "Email Notifications",
                        description = "Get updates via email",
                        icon = Icons.Outlined.Email,
                        onClick = { /* TODO */ }
                    )
                }
            }
            
            // Preferences Section
            item {
                SectionHeader(
                    title = "Preferences",
                    icon = Icons.Outlined.Settings
                )
            }
            
            item {
                SettingsCard {
                    SettingsItem(
                        title = "Language",
                        description = "English",
                        icon = Icons.Outlined.Language,
                        onClick = { /* TODO */ }
                    )
                    HorizontalDivider()
                    SettingsItem(
                        title = "Theme",
                        description = "Light mode",
                        icon = Icons.Outlined.Palette,
                        onClick = { /* TODO */ }
                    )
                }
            }
            
            // Help & Support Section
            item {
                SectionHeader(
                    title = "Help & Support",
                    icon = Icons.Outlined.Help
                )
            }
            
            item {
                SettingsCard {
                    SettingsItem(
                        title = "Help Center",
                        description = "Get help with LevelUp Journey",
                        icon = Icons.Outlined.HelpOutline,
                        onClick = { /* TODO */ }
                    )
                    HorizontalDivider()
                    SettingsItem(
                        title = "Report a Problem",
                        description = "Let us know if something isn't working",
                        icon = Icons.Outlined.BugReport,
                        onClick = { /* TODO */ }
                    )
                    HorizontalDivider()
                    SettingsItem(
                        title = "Terms of Service",
                        description = "Read our terms and conditions",
                        icon = Icons.Outlined.Description,
                        onClick = { /* TODO */ }
                    )
                }
            }
            
            // About Section
            item {
                SectionHeader(
                    title = "About",
                    icon = Icons.Outlined.Info
                )
            }
            
            item {
                SettingsCard {
                    SettingsItem(
                        title = "App Version",
                        description = "1.0.0 (Build 1)",
                        icon = Icons.Outlined.PhoneAndroid,
                        onClick = { /* TODO */ }
                    )
                    HorizontalDivider()
                    SettingsItem(
                        title = "Privacy Policy",
                        description = "Learn how we protect your data",
                        icon = Icons.Outlined.PrivacyTip,
                        onClick = { /* TODO */ }
                    )
                }
            }
            
            // Danger Zone
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }
            
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Danger Zone",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        OutlinedButton(
                            onClick = { showSignOutDialog = true },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.error
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.ExitToApp,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Sign Out")
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        OutlinedButton(
                            onClick = { showDeleteAccountDialog = true },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.error
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.DeleteForever,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Delete Account")
                        }
                    }
                }
            }
            
            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
    
    // Sign Out Dialog
    if (showSignOutDialog) {
        AlertDialog(
            onDismissRequest = { showSignOutDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Outlined.ExitToApp,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            title = { Text("Sign Out") },
            text = { Text("Are you sure you want to sign out?") },
            confirmButton = {
                Button(onClick = {
                    showSignOutDialog = false
                    onSignOut()
                }) {
                    Text("Sign Out")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSignOutDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
    
    // Delete Account Dialog
    if (showDeleteAccountDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteAccountDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            },
            title = { Text("Delete Account") },
            text = {
                Column {
                    Text("This action is permanent and cannot be undone.")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "All your data including quizzes, progress, and achievements will be permanently deleted.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteAccountDialog = false
                        // TODO: Implement delete account
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Delete Forever")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteAccountDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun SectionHeader(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun SettingsCard(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(4.dp)
        ) {
            content()
        }
    }
}

@Composable
fun SettingsItem(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = "Navigate",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
