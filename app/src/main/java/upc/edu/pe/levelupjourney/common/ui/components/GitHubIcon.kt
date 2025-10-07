package upc.edu.pe.levelupjourney.common.ui.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil.compose.AsyncImage

@Composable
fun GitHubIcon(modifier: Modifier = Modifier) {
    val isDarkTheme = isSystemInDarkTheme()
    val githubIcon = if (isDarkTheme) "file:///android_asset/github_dark.svg" else "file:///android_asset/github_light.svg"
    
    AsyncImage(
        model = githubIcon,
        contentDescription = "GitHub Logo",
        modifier = modifier
    )
}