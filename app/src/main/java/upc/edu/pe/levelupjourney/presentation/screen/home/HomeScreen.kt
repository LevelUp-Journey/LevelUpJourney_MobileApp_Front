package upc.edu.pe.levelupjourney.presentation.screen.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.LibraryBooks
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import upc.edu.pe.levelupjourney.R
import upc.edu.pe.levelupjourney.presentation.screen.home.content.HomeContent
import upc.edu.pe.levelupjourney.presentation.screen.home.join.JoinContent
import upc.edu.pe.levelupjourney.presentation.screen.home.community.CommunityContent
import upc.edu.pe.levelupjourney.presentation.screen.home.library.LibraryContent
import upc.edu.pe.levelupjourney.iam.api.ApiClient
import upc.edu.pe.levelupjourney.iam.repositories.AuthRepository
import upc.edu.pe.levelupjourney.classactivitites.repositories.QuizRepository
import upc.edu.pe.levelupjourney.classactivitites.viewmodels.QuizViewModel
import upc.edu.pe.levelupjourney.classactivitites.viewmodels.QuizViewModelFactory
import upc.edu.pe.levelupjourney.classactivitites.viewmodels.QuizState

@Composable
fun HomeScreen(
    onProfileClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onJoinTabClick: () -> Unit,
    onCommunityTabClick: () -> Unit
) {
    val context = LocalContext.current
    val authRepository = remember { AuthRepository(context, ApiClient.authApiService) }
    val quizRepository = remember { QuizRepository(ApiClient.quizApiService) }
    val viewModel: QuizViewModel = viewModel(factory = QuizViewModelFactory(quizRepository))
    
    val quizState by viewModel.quizState.collectAsState()
    val quizzes by viewModel.quizzes.collectAsState()
    
    // Check if user has quizzes (is a teacher)
    var hasQuizzes by remember { mutableStateOf(false) }
    var isCheckingQuizzes by remember { mutableStateOf(true) }
    
    LaunchedEffect(Unit) {
        val userId = authRepository.getCurrentUserId()
        if (userId != null) {
            viewModel.fetchMyQuizzes(userId)
        }
    }
    
    LaunchedEffect(quizState) {
        when (quizState) {
            is QuizState.Success -> {
                hasQuizzes = quizzes.isNotEmpty()
                isCheckingQuizzes = false
            }
            is QuizState.Error -> {
                hasQuizzes = false
                isCheckingQuizzes = false
            }
            is QuizState.Idle -> {
                isCheckingQuizzes = true
            }
            is QuizState.Loading -> {
                isCheckingQuizzes = true
            }
        }
    }
    
    var selectedTab by remember { mutableIntStateOf(0) }

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
                
                // Show Library tab only if user has quizzes (is a teacher)
                if (hasQuizzes && !isCheckingQuizzes) {
                    NavigationBarItem(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        icon = { Icon(Icons.Outlined.LibraryBooks, contentDescription = "Library") },
                        label = { Text("Library") }
                    )
                }
                
                NavigationBarItem(
                    selected = selectedTab == if (hasQuizzes && !isCheckingQuizzes) 2 else 1,
                    onClick = { selectedTab = if (hasQuizzes && !isCheckingQuizzes) 2 else 1 },
                    icon = { Icon(Icons.Outlined.Add, contentDescription = "Join") },
                    label = { Text("Join") }
                )
                NavigationBarItem(
                    selected = selectedTab == if (hasQuizzes && !isCheckingQuizzes) 3 else 2,
                    onClick = { selectedTab = if (hasQuizzes && !isCheckingQuizzes) 3 else 2 },
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
            // Top bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onProfileClick) {
                    Icon(
                        imageVector = Icons.Outlined.AccountCircle,
                        contentDescription = "Profile",
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        model = R.drawable.logo,
                        contentDescription = "App Logo",
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(Modifier.size(6.dp))
                    Text(
                        text = "Level Up Journey",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    )
                }

                IconButton(onClick = onSettingsClick) {
                    Icon(
                        imageVector = Icons.Outlined.Settings,
                        contentDescription = "Settings",
                    )
                }
            }

            // Content based on selected tab
            if (hasQuizzes && !isCheckingQuizzes) {
                // Teacher view with Library tab
                when (selectedTab) {
                    0 -> HomeContent()
                    1 -> LibraryContent()
                    2 -> JoinContent()
                    3 -> CommunityContent()
                }
            } else {
                // Student view without Library tab
                when (selectedTab) {
                    0 -> HomeContent()
                    1 -> JoinContent()
                    2 -> CommunityContent()
                }
            }
        }
    }
}