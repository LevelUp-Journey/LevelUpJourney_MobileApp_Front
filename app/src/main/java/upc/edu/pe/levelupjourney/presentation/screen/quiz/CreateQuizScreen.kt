package upc.edu.pe.levelupjourney.presentation.screen.quiz

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import upc.edu.pe.levelupjourney.classactivitites.repositories.QuizRepository
import upc.edu.pe.levelupjourney.classactivitites.viewmodels.QuizViewModel
import upc.edu.pe.levelupjourney.classactivitites.viewmodels.QuizViewModelFactory
import upc.edu.pe.levelupjourney.classactivitites.viewmodels.CreateQuizState
import upc.edu.pe.levelupjourney.iam.api.ApiClient
import upc.edu.pe.levelupjourney.iam.repositories.AuthRepository
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateQuizScreen(
    onBackClick: () -> Unit,
    onQuizCreated: (Long) -> Unit  // Navigate to questions screen with quiz ID
) {
    var quizName by remember { mutableStateOf("") }
    var quizDescription by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("PROGRAMMING") }
    var coverImageUrl by remember { mutableStateOf("") }
    var showCategoryMenu by remember { mutableStateOf(false) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    
    val animatedOffset by animateFloatAsState(
        targetValue = offsetX,
        label = "swipe"
    )
    
    val context = LocalContext.current
    val authRepository = remember { AuthRepository(context, ApiClient.authApiService) }
    val quizRepository = remember { QuizRepository(ApiClient.quizApiService) }
    val quizViewModel: QuizViewModel = viewModel(
        factory = QuizViewModelFactory(quizRepository)
    )
    
    val createQuizState by quizViewModel.createQuizState.collectAsState()
    val scope = rememberCoroutineScope()
    
    val categories = listOf(
        "PROGRAMMING",
        "SCIENCE",
        "MATHEMATICS",
        "HISTORY",
        "GEOGRAPHY",
        "LITERATURE",
        "ARTS",
        "MUSIC",
        "SPORTS",
        "GENERAL_KNOWLEDGE"
    )
    
    // Handle quiz creation result
    LaunchedEffect(createQuizState) {
        when (createQuizState) {
            is CreateQuizState.Success -> {
                val quizId = (createQuizState as CreateQuizState.Success).response.id
                Log.d("CreateQuizScreen", "Quiz created with ID: $quizId")
                quizViewModel.resetCreateQuizState()
                onQuizCreated(quizId)  // Navigate to questions screen
            }
            else -> {}
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Create New Quiz",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .offset(x = animatedOffset.dp)
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            if (abs(offsetX) > 100f) {
                                if (offsetX > 0) {
                                    onBackClick()
                                }
                            }
                            offsetX = 0f
                        },
                        onHorizontalDrag = { _, dragAmount ->
                            val newOffset = offsetX + dragAmount
                            if (newOffset >= 0) {
                                offsetX = newOffset.coerceAtMost(300f)
                            }
                        }
                    )
                }
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Quiz Name
                Column {
                    Text(
                        text = "Quiz Name *",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = quizName,
                        onValueChange = { quizName = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Enter quiz name") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                }
                
                // Description
                Column {
                    Text(
                        text = "Description",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = quizDescription,
                        onValueChange = { quizDescription = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        placeholder = { Text("Enter quiz description (optional)") },
                        maxLines = 4,
                        shape = RoundedCornerShape(12.dp)
                    )
                }
                
                // Category
                Column {
                    Text(
                        text = "Category *",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    ExposedDropdownMenuBox(
                        expanded = showCategoryMenu,
                        onExpandedChange = { showCategoryMenu = it }
                    ) {
                        OutlinedTextField(
                            value = selectedCategory,
                            onValueChange = {},
                            readOnly = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = showCategoryMenu)
                            },
                            shape = RoundedCornerShape(12.dp)
                        )
                        
                        ExposedDropdownMenu(
                            expanded = showCategoryMenu,
                            onDismissRequest = { showCategoryMenu = false }
                        ) {
                            categories.forEach { category ->
                                DropdownMenuItem(
                                    text = { Text(category) },
                                    onClick = {
                                        selectedCategory = category
                                        showCategoryMenu = false
                                    }
                                )
                            }
                        }
                    }
                }
                
                // Cover Image URL
                Column {
                    Text(
                        text = "Cover Image URL",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = coverImageUrl,
                        onValueChange = { coverImageUrl = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("https://example.com/image.jpg (optional)") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                }
                
                // Info card
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "After creating the quiz, you'll be able to add questions in the next step.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
            
            // Bottom button
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    if (createQuizState is CreateQuizState.Error) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = (createQuizState as CreateQuizState.Error).message,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }
                    
                    Button(
                        onClick = {
                            scope.launch {
                                val userId = authRepository.getCurrentUserId()
                                if (userId != null && quizName.isNotBlank()) {
                                    Log.d("CreateQuizScreen", "Creating quiz: $quizName")
                                    quizViewModel.createQuiz(
                                        name = quizName,
                                        description = quizDescription.ifBlank { null },
                                        category = selectedCategory,
                                        coverImageUrl = coverImageUrl.ifBlank { null },
                                        creatorId = userId
                                    )
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        enabled = quizName.isNotBlank() && createQuizState !is CreateQuizState.Loading,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (createQuizState is CreateQuizState.Loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Text(
                                text = "Create & Add Questions",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}
