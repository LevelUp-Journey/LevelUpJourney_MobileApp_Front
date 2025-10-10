package upc.edu.pe.levelupjourney.presentation.screen.home

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import upc.edu.pe.levelupjourney.R
import upc.edu.pe.levelupjourney.presentation.screen.home.content.HomeContent
import upc.edu.pe.levelupjourney.presentation.screen.home.join.JoinContent
import upc.edu.pe.levelupjourney.presentation.screen.home.community.CommunityContent
import upc.edu.pe.levelupjourney.iam.api.ApiClient
import upc.edu.pe.levelupjourney.iam.repositories.AuthRepository
import upc.edu.pe.levelupjourney.classactivitites.repositories.QuizRepository
import upc.edu.pe.levelupjourney.classactivitites.viewmodels.QuizViewModel
import upc.edu.pe.levelupjourney.classactivitites.viewmodels.QuizViewModelFactory
import upc.edu.pe.levelupjourney.classactivitites.viewmodels.QuizState

private val SCREEN_PADDING = 16.dp
private val CONTENT_SPACING = 8.dp

@Composable
fun HomeScreen(
    onProfileClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onJoinTabClick: () -> Unit,
    onCommunityTabClick: () -> Unit,
    onAddQuizClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val authRepository = remember { AuthRepository(context, ApiClient.authApiService) }
    val viewModel: QuizViewModel = viewModel(
        key = "homescreen_quiz_check",
        factory = QuizViewModelFactory(QuizRepository(ApiClient.quizApiService))
    )

    val quizState by viewModel.quizState.collectAsState()
    val quizzes by viewModel.quizzes.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }

    var isFetchSuccessful by remember { mutableStateOf(false) }

    // Inicialización y manejo del estado de fetch
    LaunchedEffect(Unit) {
        val userId = authRepository.getCurrentUserId()
        userId?.let {
            viewModel.fetchMyQuizzes(it)
            Log.d("HomeScreen", "Fetching quizzes for user: $it")
        } ?: Log.e("HomeScreen", "No user ID found")
    }

    // Observar el estado del fetch
    LaunchedEffect(quizState) {
        when (quizState) {
            is QuizState.Success -> {
                isFetchSuccessful = true
                Log.d("HomeScreen", "Fetch successful - ${quizzes.size} quizzes loaded")
            }
            is QuizState.Error -> {
                isFetchSuccessful = false
                Log.e("HomeScreen", "Fetch failed - ${(quizState as QuizState.Error).message}")
            }
            else -> Unit
        }
    }

    // Loading state
    if (quizState is QuizState.Loading) {
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
        topBar = { TopAppBar(onProfileClick, onSettingsClick) },
        bottomBar = { BottomNavigationBar(selectedTab) { selectedTab = it } },
        floatingActionButton = {
            if (isFetchSuccessful) {
                FloatingActionButton(
                    onClick = onAddQuizClick,
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Outlined.Add, contentDescription = "Add Quiz")
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (selectedTab) {
                0 -> HomeContent()
                1 -> JoinContent()
                2 -> CommunityContent()
                else -> HomeContent()
            }
        }
    }
}

@Composable
private fun TopAppBar(
    onProfileClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = SCREEN_PADDING, vertical = CONTENT_SPACING),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onProfileClick) {
            Icon(Icons.Outlined.AccountCircle, contentDescription = "Profile")
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(CONTENT_SPACING)
        ) {
            AsyncImage(
                model = R.drawable.logo,
                contentDescription = "App Logo",
                modifier = Modifier.size(28.dp)
            )
            Text(
                text = "Level Up Journey",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold
                )
            )
        }

        IconButton(onClick = onSettingsClick) {
            Icon(Icons.Outlined.Settings, contentDescription = "Settings")
        }
    }
}

@Composable
private fun BottomNavigationBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = selectedTab == 0,
            onClick = { onTabSelected(0) },
            icon = { Icon(Icons.Outlined.Home, contentDescription = null) },
            label = { Text("Home") }
        )

        NavigationBarItem(
            selected = selectedTab == 1,
            onClick = { onTabSelected(1) },
            icon = { Icon(Icons.Outlined.AddCircleOutline, contentDescription = null) },
            label = { Text("Join") }
        )

        NavigationBarItem(
            selected = selectedTab == 2,
            onClick = { onTabSelected(2) },
            icon = { Icon(Icons.Outlined.Group, contentDescription = null) },
            label = { Text("Community") }
        )
    }
}