package upc.edu.pe.levelupjourney.presentation.screen.main

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import upc.edu.pe.levelupjourney.presentation.components.AppHeader
import upc.edu.pe.levelupjourney.presentation.screen.join.JoinScreen
import upc.edu.pe.levelupjourney.classactivitites.domain.model.entities.Quiz
import upc.edu.pe.levelupjourney.classactivitites.repositories.QuizRepository
import upc.edu.pe.levelupjourney.classactivitites.viewmodels.QuizViewModel
import upc.edu.pe.levelupjourney.classactivitites.viewmodels.QuizViewModelFactory
import upc.edu.pe.levelupjourney.classactivitites.viewmodels.QuizState
import upc.edu.pe.levelupjourney.iam.api.ApiClient
import upc.edu.pe.levelupjourney.iam.repositories.AuthRepository
import upc.edu.pe.levelupjourney.ui.viewmodels.AuthViewModel
import upc.edu.pe.levelupjourney.ui.viewmodels.AuthViewModelFactory
import upc.edu.pe.levelupjourney.ui.viewmodels.AuthState

data class RecentGame(
    val id: String,
    val title: String,
    val description: String,
    val playersCount: Int,
    val createdAt: String
)

@Composable
fun MainScreen(
    onProfileClick: () -> Unit,
    onMenuClick: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var isAuthenticating by remember { mutableStateOf(false) }
    
    val context = LocalContext.current
    val authRepository = remember { AuthRepository(context, ApiClient.authApiService) }
    val quizRepository = remember { QuizRepository(ApiClient.quizApiService) }
    val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(authRepository))
    val quizViewModel: QuizViewModel = viewModel(factory = QuizViewModelFactory(quizRepository))
    
    val authState by authViewModel.authState.collectAsState()
    val quizState by quizViewModel.quizState.collectAsState()
    val quizzes by quizViewModel.quizzes.collectAsState()
    
    // Test user credentials - in real app, get from saved session
    val testEmail = "pcsiub@upc.edu.pe"
    val testPassword = "Teacher123"
    val testUserId = "fc10d906-0108-461c-9379-7ed43c0978bf"
    
    // Authentication flow when screen loads
    LaunchedEffect(Unit) {
        Log.d("MainScreen", "=== MAIN SCREEN INITIALIZED ===")
        Log.d("MainScreen", "Starting authentication process...")
        
        // Check if already authenticated, if not, login first
        isAuthenticating = true
        Log.d("MainScreen", "Attempting login with email: $testEmail")
        authViewModel.signIn(testEmail, testPassword)
    }
    
    // Watch auth state to fetch quizzes after successful login
    LaunchedEffect(authState) {
        Log.d("MainScreen", "=== AUTH STATE CHANGED ===")
        Log.d("MainScreen", "Auth State: ${authState.javaClass.simpleName}")
        Log.d("MainScreen", "Is Authenticating: $isAuthenticating")
        
        when (authState) {
            is AuthState.Success -> {
                if (isAuthenticating) {
                    Log.d("MainScreen", "=== AUTHENTICATION SUCCESSFUL ===")
                    Log.d("MainScreen", "Now fetching quizzes for user: $testUserId")
                    isAuthenticating = false
                    // Now that we're authenticated, fetch quizzes
                    quizViewModel.fetchMyQuizzes(testUserId)
                } else {
                    Log.d("MainScreen", "Auth success but not in authenticating state")
                }
            }
            is AuthState.Error -> {
                if (isAuthenticating) {
                    Log.e("MainScreen", "=== AUTHENTICATION FAILED ===")
                    Log.e("MainScreen", "Auth error: ${(authState as AuthState.Error).message}")
                    isAuthenticating = false
                } else {
                    Log.e("MainScreen", "Auth error but not in authenticating state")
                }
            }
            is AuthState.Loading -> {
                Log.d("MainScreen", "Authentication in progress...")
            }
            else -> {
                Log.d("MainScreen", "Auth state: ${authState.javaClass.simpleName}")
            }
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Outlined.Home, contentDescription = "Home") },
                    label = { Text("Home") }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Outlined.Add, contentDescription = "Join") },
                    label = { Text("Join") }
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Icon(Icons.Outlined.Group, contentDescription = "Community") },
                    label = { Text("Community") }
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Header
            AppHeader(
                onProfileClick = onProfileClick,
                onMenuClick = onMenuClick
            )
            
            // Content based on selected tab
            when (selectedTab) {
                0 -> HomeContent(
                    authState = authState,
                    quizState = quizState,
                    quizzes = quizzes,
                    isAuthenticating = isAuthenticating,
                    onRefresh = { 
                        Log.d("MainScreen", "=== REFRESH TRIGGERED ===")
                        if (authState is AuthState.Success) {
                            Log.d("MainScreen", "Auth is success, fetching quizzes for user: $testUserId")
                            quizViewModel.fetchMyQuizzes(testUserId)
                        } else {
                            Log.d("MainScreen", "Auth not success, re-authenticating...")
                            Log.d("MainScreen", "Current auth state: ${authState.javaClass.simpleName}")
                            // Re-authenticate if needed
                            authViewModel.signIn(testEmail, testPassword)
                        }
                    }
                )
                1 -> JoinScreen(
                    onQRScanClick = { /* TODO: Navigate to QR scan */ },
                    onPinJoinClick = { /* TODO: Navigate to PIN join */ }
                )
                2 -> CommunityContent()
            }
        }
    }
}

@Composable
fun HomeContent(
    authState: AuthState,
    quizState: QuizState,
    quizzes: List<Quiz>,
    isAuthenticating: Boolean,
    onRefresh: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Recent Games Section
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "My Quizzes",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                ),
                color = MaterialTheme.colorScheme.onBackground
            )
            
            IconButton(onClick = onRefresh) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = "Refresh",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Show authentication status
        if (isAuthenticating || authState is AuthState.Loading) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = if (isAuthenticating) "Autenticando..." else "Cargando quizzes...",
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        // Show auth error if any
        if (authState is AuthState.Error) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Error de autenticación",
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = authState.message,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onRefresh,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text("Reintentar")
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        // Quiz content (only show when authenticated)
        if (authState is AuthState.Success) {
            when (quizState) {
                is QuizState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                }
                
                is QuizState.Error -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Error loading quizzes",
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = quizState.message,
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = onRefresh,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                Text("Retry")
                            }
                        }
                    }
                }
                
                is QuizState.Success -> {
                    if (quizzes.isEmpty()) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "No quizzes found",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Create your first quiz to get started!",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(quizzes) { quiz ->
                                QuizCard(quiz = quiz)
                            }
                        }
                    }
                }
                
                else -> {
                    // Idle state - show loading
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Initializing...")
                    }
                }
            }
        }
    }
}

@Composable
fun QuizCard(quiz: Quiz) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = quiz.name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = quiz.description ?: "No description",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = quiz.category,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = quiz.visibility,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Created: ${quiz.createdAt.take(10)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Button(
                    onClick = { /* Navigate to quiz details */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Ver detalles",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}



@Composable
fun CommunityContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Community Content\n(En desarrollo)",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}