package upc.edu.pe.levelupjourney.presentation.screen.welcome

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun WelcomeScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = onNavigateToRegister,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create Account")
        }

        Spacer(Modifier.height(16.dp))

        //Google (placeholder)
        Button(
            onClick = { /* TODO: Google OAuth */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.AccountCircle, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Sign up with Google")
        }

        Spacer(Modifier.height(16.dp))

        //GitHub (placeholder)
        Button(
            onClick = { /* TODO: GitHub OAuth */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Code, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Sign up with Github")
        }

        Spacer(Modifier.height(24.dp))

        TextButton(onClick = onNavigateToLogin) {
            Text("Already have an account? Sign In")
        }
    }
}
