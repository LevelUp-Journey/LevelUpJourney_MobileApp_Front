package upc.edu.pe.levelupjourney.presentation.screen.main

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import upc.edu.pe.levelupjourney.presentation.components.AppHeader
import upc.edu.pe.levelupjourney.classactivitites.domain.model.entities.Quiz
import upc.edu.pe.levelupjourney.classactivitites.repositories.QuizRepository
import upc.edu.pe.levelupjourney.classactivitites.viewmodels.QuizViewModel
import upc.edu.pe.levelupjourney.classactivitites.viewmodels.QuizViewModelFactory
import upc.edu.pe.levelupjourney.classactivitites.viewmodels.QuizState
import upc.edu.pe.levelupjourney.iam.api.ApiClient
import upc.edu.pe.levelupjourney.iam.repositories.AuthRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onProfileClick: () -> Unit,
    onMenuClick: () -> Unit,
    onCreateQuizClick: () -> Unit = {}
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var showJoinDrawer by remember { mutableStateOf(false) }
    var isTeacher by remember { mutableStateOf(false) }
    var hasQuizzesCheck by remember { mutableStateOf(false) }
    
    val context = LocalContext.current
    val authRepository = remember { AuthRepository(context, ApiClient.authApiService) }
    val quizRepository = remember { QuizRepository(ApiClient.quizApiService) }
    val quizViewModel: QuizViewModel = viewModel(
        key = "mainscreen_quiz_check",
        factory = QuizViewModelFactory(quizRepository)
    )
    
    val quizState by quizViewModel.quizState.collectAsState()
    val quizzes by quizViewModel.quizzes.collectAsState()
    val scope = rememberCoroutineScope()
    
    LaunchedEffect(Unit) {
        scope.launch {
            Log.d("MainScreen", "=== INITIALIZING MAIN SCREEN ===")
            val cachedRole = authRepository.getUserRole()
            Log.d("MainScreen", "Cached role: $cachedRole")
            
            if (cachedRole == "ROLE_TEACHER") {
                isTeacher = true
                hasQuizzesCheck = true
                Log.d("MainScreen", "User is TEACHER (from cache)")
            } else {
                val userId = authRepository.getCurrentUserId()
                if (userId != null) {
                    Log.d("MainScreen", "Fetching quizzes to check teacher status for user: $userId")
                    quizViewModel.fetchMyQuizzes(userId)
                }
            }
        }
    }
    
    LaunchedEffect(quizState) {
        if (!hasQuizzesCheck) {
            when (quizState) {
                is QuizState.Success -> {
                    scope.launch {
                        hasQuizzesCheck = true
                        isTeacher = true
                        authRepository.saveUserRole("ROLE_TEACHER")
                        Log.d("MainScreen", "✅ User is TEACHER (fetch successful)")
                    }
                }
                is QuizState.Error -> {
                    scope.launch {
                        hasQuizzesCheck = true
                        isTeacher = false
                        authRepository.saveUserRole("ROLE_STUDENT")
                        Log.d("MainScreen", "👤 User is STUDENT (fetch failed)")
                    }
                }
                else -> {}
            }
        }
    }
    
    if (!hasQuizzesCheck) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
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
                
                if (isTeacher) {
                    NavigationBarItem(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        icon = { Icon(Icons.Outlined.LibraryBooks, contentDescription = "Library") },
                        label = { Text("Library") }
                    )
                }
                
                NavigationBarItem(
                    selected = false,
                    onClick = { showJoinDrawer = true },
                    icon = { Icon(Icons.Outlined.Add, contentDescription = "Join") },
                    label = { Text("Join") }
                )
                
                NavigationBarItem(
                    selected = selectedTab == if (isTeacher) 3 else 2,
                    onClick = { selectedTab = if (isTeacher) 3 else 2 },
                    icon = { Icon(Icons.Outlined.Group, contentDescription = "Community") },
                    label = { Text("Community") }
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                AppHeader(
                    onProfileClick = onProfileClick,
                    onMenuClick = onMenuClick
                )
                
                if (isTeacher) {
                    when (selectedTab) {
                        0 -> HomeContent(quizState, quizzes, authRepository, quizViewModel)
                        1 -> LibraryContent(
                            quizzes = quizzes,
                            onCreateQuizClick = onCreateQuizClick
                        )
                        3 -> CommunityContent()
                    }
                } else {
                    when (selectedTab) {
                        0 -> HomeContent(quizState, quizzes, authRepository, quizViewModel)
                        2 -> CommunityContent()
                    }
                }
            }
            
            if (showJoinDrawer) {
                JoinGameDrawer(
                    onDismiss = { showJoinDrawer = false }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JoinGameDrawer(
    onDismiss: () -> Unit
) {
    var pin by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true  // Forzar expansión completa
    )
    
    LaunchedEffect(Unit) {
        sheetState.expand()  // Expandir completamente al abrir
    }
    
    ModalBottomSheet(
        onDismissRequest = { 
            onDismiss()
            pin = ""
        },
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 56.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Join Game",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Text(
                text = "Enter the game PIN or scan QR code",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 32.dp)
            )
            
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "PIN",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                OutlinedTextField(
                    value = pin,
                    onValueChange = { 
                        if (it.length <= 6) {
                            pin = it
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(80.dp),
                    textStyle = MaterialTheme.typography.displaySmall.copy(
                        textAlign = TextAlign.Center,
                        letterSpacing = 12.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                            if (pin.length >= 4) {
                                // TODO: Join game with PIN
                            }
                        }
                    ),
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    ),
                    placeholder = {
                        Text(
                            text = "000000",
                            style = MaterialTheme.typography.displaySmall.copy(
                                textAlign = TextAlign.Center,
                                letterSpacing = 12.sp
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                        )
                    }
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Button(
                    onClick = {
                        if (pin.length >= 4) {
                            // TODO: Join game
                            keyboardController?.hide()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(56.dp),
                    enabled = pin.length >= 4,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Text(
                        text = "Join",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f))
                Text(
                    text = "OR",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                HorizontalDivider(modifier = Modifier.weight(1f))
            }
            
            OutlinedButton(
                onClick = { 
                    // TODO: Open QR scanner
                    keyboardController?.hide()
                },
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    imageVector = Icons.Outlined.QrCodeScanner,
                    contentDescription = "Scan QR Code",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Scan QR Code",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun HomeContent(
    quizState: QuizState,
    quizzes: List<Quiz>,
    authRepository: AuthRepository,
    quizViewModel: QuizViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "My Quizzes",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            ),
            color = MaterialTheme.colorScheme.onBackground
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        when (quizState) {
            is QuizState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
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
                                text = "Create your first quiz!",
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
                            QuizCard(quiz)
                        }
                    }
                }
            }
            
            is QuizState.Error -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Error loading quizzes",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = (quizState as QuizState.Error).message,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }
            
            else -> {}
        }
    }
}

@Composable
fun LibraryContent(
    quizzes: List<Quiz>,
    onCreateQuizClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Library",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
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
                            text = "No quizzes in library",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Create your first quiz to get started!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(quizzes) { quiz ->
                        QuizCard(quiz)
                    }
                }
            }
        }
        
        FloatingActionButton(
            onClick = onCreateQuizClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(
                imageVector = Icons.Outlined.Add,
                contentDescription = "Create Quiz",
                tint = MaterialTheme.colorScheme.onPrimary
            )
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
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = quiz.visibility,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.primary
            )
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
            text = "Community\n(Coming soon)",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
    }
}
