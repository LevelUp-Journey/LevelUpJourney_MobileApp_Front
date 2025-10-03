package upc.edu.pe.levelupjourney.presentation.screen.game

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun GameFinishedScreen(
    onBackToHome: () -> Unit
) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Quiz finished!\nWaiting for results...")
            Spacer(Modifier.height(24.dp))
            Button(onClick = onBackToHome) {
                Text("Back to Home")
            }
        }
    }
}
