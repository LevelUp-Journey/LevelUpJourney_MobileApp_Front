package upc.edu.pe.levelupjourney.presentation.screen.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onSignOut: () -> Unit
) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Settings",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black
                )
            }

            Text(
                text = "Account",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(Modifier.height(16.dp))

            SettingsItem("Delete account", onClick = { /* TODO: acción borrar cuenta */ })
            Divider()

            SettingsItem("Sign out", onClick = onSignOut)
            Divider()

            Spacer(Modifier.height(16.dp))
            Text(
                text = "Help",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            SettingsItem("Version", onClick = { /* TODO: mostrar versión */ })
            Divider()

            SettingsItem("FAQ", onClick = { /* TODO: abrir FAQ */ })
            Divider()

            SettingsItem("Support", onClick = { /* TODO: abrir soporte */ })
            Divider()
        }
    }
}

@Composable
fun SettingsItem(title: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(Color(0xFFF5EEFF), RoundedCornerShape(4.dp))
            .padding(16.dp)
    ) {
        Text(text = title, color = Color.Black)
    }
}
