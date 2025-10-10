package upc.edu.pe.levelupjourney

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import upc.edu.pe.levelupjourney.common.ui.theme.AppTheme
import upc.edu.pe.levelupjourney.presentation.navigation.AppNavHost
import upc.edu.pe.levelupjourney.iam.api.ApiClient
import upc.edu.pe.levelupjourney.iam.repositories.AuthRepository

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize ApiClient with AuthRepository for token injection
        val authRepository = AuthRepository(applicationContext, ApiClient.authApiService)
        ApiClient.initialize(authRepository)
        
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