package upc.edu.pe.levelupjourney.presentation.screen.join

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import coil.compose.AsyncImage
import upc.edu.pe.levelupjourney.R

@Composable
fun ConnectingScreen(
    onFinished: () -> Unit
) {
    // animación de rotación
    val rotation = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        rotation.animateTo(
            targetValue = 360f,
            animationSpec = tween(durationMillis = 2000, easing = LinearEasing)
        )
        delay(200) // pequeño delay antes de continuar
        onFinished()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Connecting")
        Spacer(Modifier.height(24.dp))
        AsyncImage(
            model = R.drawable.logo,
            contentDescription = "Logo",
            modifier = Modifier
                .size(80.dp)
                .rotate(rotation.value)
        )
    }
}
