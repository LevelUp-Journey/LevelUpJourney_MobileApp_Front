package upc.edu.pe.levelupjourney.presentation.screen.join

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import upc.edu.pe.levelupjourney.R
import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.ui.res.painterResource

@Composable
fun NicknameScreen(
    onBackToHome: () -> Unit,
    onEnterNickname: (String) -> Unit
) {
    var nickname by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackToHome) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close"
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier.size(80.dp)
            )
            Spacer(Modifier.height(24.dp))
            OutlinedTextField(
                value = nickname,
                onValueChange = { nickname = it },
                label = { Text("Nickname") }
            )
            Spacer(Modifier.height(24.dp))
            Button(onClick = { onEnterNickname(nickname) }, enabled = nickname.isNotBlank()) {
                Text("ENTER")
            }
        }
    }
}
