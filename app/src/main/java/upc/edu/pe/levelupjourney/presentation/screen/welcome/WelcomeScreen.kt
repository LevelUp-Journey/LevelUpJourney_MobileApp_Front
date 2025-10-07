package upc.edu.pe.levelupjourney.presentation.screen.welcome

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import upc.edu.pe.levelupjourney.R

@Composable
fun WelcomeScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF32A27),
                        Color(0xFFE53E3B),
                        Color(0xFFD21E1B)
                    )
                )
            )
    ) {
        // Main content area
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 32.dp, vertical = 40.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // App Logo
            Image(
                painter = painterResource(id = R.drawable.pet_sat),
                contentDescription = "LevelUp Journey Logo",
                modifier = Modifier
                    .size(120.dp)
                    .padding(bottom = 16.dp)
            )
            
            // App Title
            Text(
                text = "LevelUp Journey",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp
                ),
                color = Color.White,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // App Subtitle
            Text(
                text = "Transform your classroom into an interactive gaming experience",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.9f),
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )
        }
        
        // Bottom Drawer
        BottomRegistrationDrawer(
            onNavigateToLogin = onNavigateToLogin,
            onNavigateToRegister = onNavigateToRegister,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun BottomRegistrationDrawer(
    onNavigateToLogin: () -> Unit,
    onNavigateToRegister: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Drawer handle
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(4.dp)
                    .background(
                        color = Color.Gray.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(2.dp)
                    )
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Title
            Text(
                text = "Get Started",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color.Black,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Join thousands of educators revolutionizing their classrooms",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Create Account Button
            Button(
                onClick = onNavigateToRegister,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF32A27)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    Icons.Default.Code,
                    contentDescription = "GitHub Logo",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Create Account",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = Color.White
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Google Sign Up Button
            OutlinedButton(
                onClick = { /* TODO: Google OAuth */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    width = 1.dp
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                AsyncImage(
                    model = "file:///android_asset/google.svg",
                    contentDescription = "Google Logo",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Continue with Google",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = Color(0xFFF32A27)
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // GitHub Sign Up Button
            OutlinedButton(
                onClick = { /* TODO: GitHub OAuth */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    width = 1.dp
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                AsyncImage(
                    model = "file:///android_asset/github_light.svg",
                    contentDescription = "GitHub Logo",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Continue with GitHub",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = Color(0xFFF32A27)
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Sign In Link
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Already have an account? ",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                TextButton(
                    onClick = onNavigateToLogin,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = "Sign In",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = Color(0xFFF32A27)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
