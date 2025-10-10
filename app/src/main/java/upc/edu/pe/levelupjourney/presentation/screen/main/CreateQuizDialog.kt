package upc.edu.pe.levelupjourney.presentation.screen.main

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.launch
import upc.edu.pe.levelupjourney.classactivitites.viewmodels.QuizViewModel
import upc.edu.pe.levelupjourney.classactivitites.viewmodels.CreateQuizState
import upc.edu.pe.levelupjourney.iam.repositories.AuthRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateQuizDialog(
    authRepository: AuthRepository,
    quizViewModel: QuizViewModel,
    onDismiss: () -> Unit
) {
    var quizName by remember { mutableStateOf("") }
    var quizDescription by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("PROGRAMMING") }
    var coverImageUrl by remember { mutableStateOf("") }
    var showCategoryMenu by remember { mutableStateOf(false) }
    
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
                Log.d("CreateQuizDialog", "Quiz created successfully")
                // Refresh the quiz list
                scope.launch {
                    val userId = authRepository.getCurrentUserId()
                    if (userId != null) {
                        quizViewModel.fetchMyQuizzes(userId)
                    }
                }
                quizViewModel.resetCreateQuizState()
                onDismiss()
            }
            else -> {}
        }
    }
    
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.85f),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Create New Quiz",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = "Close"
                        )
                    }
                }
                
                HorizontalDivider()
                
                // Content
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
                    
                    // Cover Image URL (optional)
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
                    
                    // Info text
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "After creating the quiz, you can add questions in the next step.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
                
                // Footer with buttons
                HorizontalDivider()
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Cancel")
                    }
                    
                    Button(
                        onClick = {
                            scope.launch {
                                val userId = authRepository.getCurrentUserId()
                                if (userId != null && quizName.isNotBlank()) {
                                    Log.d("CreateQuizDialog", "Creating quiz: $quizName")
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
                        modifier = Modifier.weight(1f),
                        enabled = quizName.isNotBlank() && createQuizState !is CreateQuizState.Loading,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (createQuizState is CreateQuizState.Loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Text("Create Quiz")
                        }
                    }
                }
                
                // Show error if any
                if (createQuizState is CreateQuizState.Error) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(bottom = 16.dp),
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
            }
        }
    }
}
