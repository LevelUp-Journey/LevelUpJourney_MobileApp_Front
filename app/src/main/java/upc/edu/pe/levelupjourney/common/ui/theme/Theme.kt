package upc.edu.pe.levelupjourney.common.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val RedLightColors = lightColorScheme(
    primary = Color(0xFFF32A27),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFFFDAD6),
    onPrimaryContainer = Color(0xFF410002),
    
    secondary = Color(0xFFFF6B68),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFFFDAD6),
    onSecondaryContainer = Color(0xFF2C1512),
    
    tertiary = Color(0xFFE53E3B),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFFFDAD6),
    onTertiaryContainer = Color(0xFF2C1512),
    
    error = Color(0xFFB00020),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    
    background = Color(0xFFFFFBFB),
    onBackground = Color(0xFF201A1A),
    surface = Color(0xFFFFF5F5),
    onSurface = Color(0xFF201A1A),
    surfaceVariant = Color(0xFFF5DDDA),
    onSurfaceVariant = Color(0xFF534341),
    
    outline = Color(0xFF857371),
    outlineVariant = Color(0xFFD8C2BE),
    scrim = Color(0xFF000000),
    inverseSurface = Color(0xFF362F2E),
    inverseOnSurface = Color(0xFFFBEEEC),
    inversePrimary = Color(0xFFFFB4AB)
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = RedLightColors,
        typography = MaterialTheme.typography,
        content = content
    )
}
