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
import upc.edu.pe.levelupjourney.presentation.screen.home.library.LibraryContent
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

    // Estado para determinar si el fetch fue exitoso (independiente de si tiene quizzes)
    var isFetchSuccessful by remember { mutableStateOf(false) }
    // Estado para determinar si tiene quizzes (para mostrar Library tab)
    var hasQuizzes by remember { mutableStateOf(false) }

    // Inicialización y manejo del estado de fetch
    LaunchedEffect(Unit) {
        val userId = authRepository.getCurrentUserId()
        Log.d("HomeScreen", "=== INITIALIZING HOME SCREEN ===")
        Log.d("HomeScreen", "User ID: $userId")
        userId?.let {
            viewModel.fetchMyQuizzes(it)
        } ?: run {
            Log.e("HomeScreen", "No user ID found")
            isFetchSuccessful = false
        }
    }

    // Observar el estado del fetch y actualizar flags
    LaunchedEffect(quizState) {
        Log.d("HomeScreen", "=== QUIZ STATE CHANGED ===")
        Log.d("HomeScreen", "State: $quizState")
        
        when (quizState) {
            is QuizState.Success -> {
                isFetchSuccessful = true  // ✅ Fetch exitoso (incluso si array vacío)
                hasQuizzes = quizzes.isNotEmpty()  // Solo true si tiene quizzes
                Log.d("HomeScreen", "✅ Fetch SUCCESSFUL")
                Log.d("HomeScreen", "Quizzes count: ${quizzes.size}")
                Log.d("HomeScreen", "Has quizzes: $hasQuizzes")
                Log.d("HomeScreen", "Show FAB: true (fetch successful)")
                Log.d("HomeScreen", "Show Library tab: $hasQuizzes")
            }
            is QuizState.Error -> {
                isFetchSuccessful = false  // ❌ Fetch falló (no autorizado u otro error)
                hasQuizzes = false
                Log.e("HomeScreen", "❌ Fetch FAILED")
                Log.e("HomeScreen", "Error: ${(quizState as QuizState.Error).message}")
                Log.d("HomeScreen", "Show FAB: false (fetch failed)")
                Log.d("HomeScreen", "Show Library tab: false")
            }
            is QuizState.Loading -> {
                Log.d("HomeScreen", "⏳ LOADING - Fetching quizzes...")
            }
            is QuizState.Idle -> {
                Log.d("HomeScreen", "⏸️ IDLE - Waiting to fetch")
            }
        }
    }

    // Loading state - mostrar mientras se hace el fetch inicial
    if (quizState is QuizState.Loading || quizState is QuizState.Idle) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    Log.d("HomeScreen", "=== RENDERING HOME SCREEN ===")
    Log.d("HomeScreen", "isFetchSuccessful: $isFetchSuccessful")
    Log.d("HomeScreen", "hasQuizzes: $hasQuizzes")

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { TopAppBar(onProfileClick, onSettingsClick) },
        bottomBar = { 
            BottomNavigationBar(
                selectedTab = selectedTab,
                hasQuizzes = hasQuizzes,
                onTabSelected = { selectedTab = it }
            ) 
        },
        floatingActionButton = {
            // FAB solo se muestra si el fetch fue exitoso (incluso con array vacío)
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
            // Renderizar contenido según el tab seleccionado
            if (hasQuizzes) {
                // Vista con Library tab
                when (selectedTab) {
                    0 -> HomeContent()
                    1 -> LibraryContent()
                    2 -> JoinContent()
                    3 -> CommunityContent()
                    else -> HomeContent()
                }
            } else {
                // Vista sin Library tab
                when (selectedTab) {
                    0 -> HomeContent()
                    1 -> JoinContent()
                    2 -> CommunityContent()
                    else -> HomeContent()
                }
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
    hasQuizzes: Boolean,
    onTabSelected: (Int) -> Unit
) {
    NavigationBar {
        // Home (siempre visible)
        NavigationBarItem(
            selected = selectedTab == 0,
            onClick = { onTabSelected(0) },
            icon = { Icon(Icons.Outlined.Home, contentDescription = null) },
            label = { Text("Home") }
        )

        // Library (solo si tiene quizzes)
        if (hasQuizzes) {
            NavigationBarItem(
                selected = selectedTab == 1,
                onClick = { onTabSelected(1) },
                icon = { Icon(Icons.Outlined.LibraryBooks, contentDescription = null) },
                label = { Text("Library") }
            )
        }

        // Join (índice cambia según si Library está visible)
        NavigationBarItem(
            selected = selectedTab == if (hasQuizzes) 2 else 1,
            onClick = { onTabSelected(if (hasQuizzes) 2 else 1) },
            icon = { Icon(Icons.Outlined.AddCircleOutline, contentDescription = null) },
            label = { Text("Join") }
        )

        // Community (índice cambia según si Library está visible)
        NavigationBarItem(
            selected = selectedTab == if (hasQuizzes) 3 else 2,
            onClick = { onTabSelected(if (hasQuizzes) 3 else 2) },
            icon = { Icon(Icons.Outlined.Group, contentDescription = null) },
            label = { Text("Community") }
        )
    }
}