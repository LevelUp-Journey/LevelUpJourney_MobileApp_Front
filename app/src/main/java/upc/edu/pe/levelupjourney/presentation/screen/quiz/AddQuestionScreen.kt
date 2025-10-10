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
import upc.edu.pe.levelupjourney.classactivitites.viewmodels.QuestionState
import upc.edu.pe.levelupjourney.iam.api.ApiClient
import upc.edu.pe.levelupjourney.iam.repositories.AuthRepository
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddQuestionScreen(
    quizId: Long,
    onBackClick: () -> Unit,
    onQuestionAdded: () -> Unit
) {
    var questionText by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf("MULTIPLE_CHOICE") }
    var timeLimit by remember { mutableStateOf("30") }
    var points by remember { mutableStateOf("10") }
    var mediaUrl by remember { mutableStateOf("") }
    var showTypeMenu by remember { mutableStateOf(false) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    
    val animatedOffset by animateFloatAsState(
        targetValue = offsetX,
        label = "swipe"
    )
    
    // Answers
    var answers by remember { mutableStateOf(listOf("", "", "", "")) }
    var correctAnswerIndex by remember { mutableIntStateOf(0) }
    
    val context = LocalContext.current
    val authRepository = remember { AuthRepository(context, ApiClient.authApiService) }
    val quizRepository = remember { QuizRepository(ApiClient.quizApiService) }
    val quizViewModel: QuizViewModel = viewModel(
        key = "add_question_$quizId",
        factory = QuizViewModelFactory(quizRepository)
    )
    
    val questionState by quizViewModel.questionState.collectAsState()
    val scope = rememberCoroutineScope()
    
    val questionTypes = listOf("MULTIPLE_CHOICE", "TRUE_FALSE")
    
    // Handle question creation result
    LaunchedEffect(questionState) {
        when (questionState) {
            is QuestionState.Success -> {
                Log.d("AddQuestionScreen", "Question added successfully")
                quizViewModel.resetQuestionState()
                onQuestionAdded()
            }
            else -> {}
        }
    }
    
    // Update answers list based on type
    LaunchedEffect(selectedType) {
        answers = if (selectedType == "TRUE_FALSE") {
            listOf("True", "False")
        } else {
            listOf("", "", "", "")
        }
        correctAnswerIndex = 0
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Add Question",
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
                // Question Type
                Column {
                    Text(
                        text = "Question Type *",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    ExposedDropdownMenuBox(
                        expanded = showTypeMenu,
                        onExpandedChange = { showTypeMenu = it }
                    ) {
                        OutlinedTextField(
                            value = selectedType,
                            onValueChange = {},
                            readOnly = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = showTypeMenu)
                            },
                            shape = RoundedCornerShape(12.dp)
                        )
                        
                        ExposedDropdownMenu(
                            expanded = showTypeMenu,
                            onDismissRequest = { showTypeMenu = false }
                        ) {
                            questionTypes.forEach { type ->
                                DropdownMenuItem(
                                    text = { Text(type) },
                                    onClick = {
                                        selectedType = type
                                        showTypeMenu = false
                                    }
                                )
                            }
                        }
                    }
                }
                
                // Question Text
                Column {
                    Text(
                        text = "Question Text *",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = questionText,
                        onValueChange = { questionText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        placeholder = { Text("Enter your question") },
                        maxLines = 3,
                        shape = RoundedCornerShape(12.dp)
                    )
                }
                
                // Time Limit & Points
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Time Limit (seconds) *",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = timeLimit,
                            onValueChange = { if (it.all { char -> char.isDigit() }) timeLimit = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("30") },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                    
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Points *",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = points,
                            onValueChange = { if (it.all { char -> char.isDigit() }) points = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("10") },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }
                
                // Answers
                Column {
                    Text(
                        text = "Answers *",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    answers.forEachIndexed { index, answer ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            RadioButton(
                                selected = correctAnswerIndex == index,
                                onClick = { correctAnswerIndex = index }
                            )
                            
                            OutlinedTextField(
                                value = answer,
                                onValueChange = { newValue ->
                                    answers = answers.toMutableList().apply {
                                        this[index] = newValue
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                placeholder = { Text("Answer ${index + 1}") },
                                singleLine = true,
                                enabled = selectedType != "TRUE_FALSE",
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                        if (index < answers.size - 1) {
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                    
                    if (selectedType == "MULTIPLE_CHOICE" && answers.size < 6) {
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedButton(
                            onClick = {
                                answers = answers + ""
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("+ Add Answer")
                        }
                    }
                }
                
                // Media URL
                Column {
                    Text(
                        text = "Media URL (optional)",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = mediaUrl,
                        onValueChange = { mediaUrl = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("https://example.com/image.jpg") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
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
                    if (questionState is QuestionState.Error) {
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
                                text = (questionState as QuestionState.Error).message,
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
                                val timeLimitInt = timeLimit.toIntOrNull() ?: 30
                                val pointsInt = points.toIntOrNull() ?: 10
                                
                                val validAnswers = if (selectedType == "TRUE_FALSE") {
                                    listOf("True", "False")
                                } else {
                                    answers.filter { it.isNotBlank() }
                                }
                                
                                if (userId != null && questionText.isNotBlank() && validAnswers.size >= 2) {
                                    Log.d("AddQuestionScreen", "Adding question to quiz $quizId")
                                    quizViewModel.addQuestion(
                                        quizId = quizId,
                                        questionText = questionText,
                                        questionType = selectedType,
                                        timeLimit = timeLimitInt,
                                        points = pointsInt,
                                        answers = validAnswers,
                                        correctAnswerIndex = correctAnswerIndex,
                                        mediaUrl = mediaUrl.ifBlank { null },
                                        userId = userId
                                    )
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        enabled = questionText.isNotBlank() && 
                                  answers.filter { it.isNotBlank() }.size >= 2 && 
                                  questionState !is QuestionState.Loading,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (questionState is QuestionState.Loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Text(
                                text = "Add Question",
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
