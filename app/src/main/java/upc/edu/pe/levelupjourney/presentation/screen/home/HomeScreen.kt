package upc.edu.pe.levelupjourney.presentation.screen.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import upc.edu.pe.levelupjourney.R
import upc.edu.pe.levelupjourney.presentation.screen.home.content.HomeContent
import upc.edu.pe.levelupjourney.presentation.screen.home.join.JoinContent
import upc.edu.pe.levelupjourney.presentation.screen.home.community.CommunityContent

@Composable
fun HomeScreen(
    onProfileClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onJoinTabClick: () -> Unit,
    onCommunityTabClick: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Outlined.Home, contentDescription = "Home") },
                    label = { Text("Home") }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Outlined.Add, contentDescription = "Join") },
                    label = { Text("Join") }
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Icon(Icons.Outlined.Group, contentDescription = "Community") },
                    label = { Text("Community") }
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Top bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onProfileClick) {
                    Icon(
                        imageVector = Icons.Outlined.AccountCircle,
                        contentDescription = "Profile",
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        model = R.drawable.logo,
                        contentDescription = "App Logo",
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(Modifier.size(6.dp))
                    Text(
                        text = "Level Up Journey",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    )
                }

                IconButton(onClick = onSettingsClick) {
                    Icon(
                        imageVector = Icons.Outlined.Settings,
                        contentDescription = "Settings",
                    )
                }
            }

            // Content based on selected tab
            when (selectedTab) {
                0 -> HomeContent()
                1 -> JoinContent()
                2 -> CommunityContent()
            }
        }
    }
}