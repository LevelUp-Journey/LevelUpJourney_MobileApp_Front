package upc.edu.pe.levelupjourney

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import upc.edu.pe.levelupjourney.common.ui.theme.AppTheme
import upc.edu.pe.levelupjourney.presentation.navigation.AppNavHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                AppNavHost()
            }
        }
    }
}


@Preview
@Composable
fun MainPreview() {
    AppTheme {
        AppNavHost()
    }
}