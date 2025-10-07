package upc.edu.pe.levelupjourney.presentation.screen.home.join

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun JoinContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Join Game",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Text(
            text = "Enter PIN",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OutlinedButton(
                onClick = { /* Navigate to PIN entry */ },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
            ) {
                Text("Enter PIN")
            }

            Spacer(modifier = Modifier.width(16.dp))

            OutlinedButton(
                onClick = { /* Navigate to QR scanner */ },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.QrCodeScanner,
                    contentDescription = "QR Scanner",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Scan QR")
            }
        }
    }
}