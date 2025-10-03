package upc.edu.pe.levelupjourney.presentation.screen.game

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

data class Question(
    val id: Int,
    val title: String,
    val placeholder: String,
    val options: List<String>,
    val correctIndex: Int
)


@Composable
fun GameQuestionScreen(
    question: Question,
    onClose: () -> Unit,
    onNext: () -> Unit
) {
    var selectedAnswer by remember { mutableStateOf<Int?>(null) }
    var showFeedback by remember { mutableStateOf(false) }
    var isCorrect by remember { mutableStateOf(false) }

    if (showFeedback) {
        FeedbackScreen(
            isCorrect = isCorrect,
            onNext = onNext
        )
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Número de la pregunta
            Text(
                text = "${question.id}",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Texto de la pregunta
            Text(
                text = question.title,
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Placeholder (imagen o bloque)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(Color.LightGray, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(question.placeholder)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Opciones
            if (question.options.size == 4) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        AnswerButton(
                            text = question.options[0],
                            color = Color.Blue,
                            onClick = {
                                selectedAnswer = 0
                                isCorrect = selectedAnswer == question.correctIndex
                                showFeedback = true
                            },
                            modifier = Modifier.weight(1f)
                        )
                        AnswerButton(
                            text = question.options[1],
                            color = Color.Red,
                            onClick = {
                                selectedAnswer = 1
                                isCorrect = selectedAnswer == question.correctIndex
                                showFeedback = true
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        AnswerButton(
                            text = question.options[2],
                            color = Color.Green,
                            onClick = {
                                selectedAnswer = 2
                                isCorrect = selectedAnswer == question.correctIndex
                                showFeedback = true
                            },
                            modifier = Modifier.weight(1f)
                        )
                        AnswerButton(
                            text = question.options[3],
                            color = Color(0xFFFFA500),
                            onClick = {
                                selectedAnswer = 3
                                isCorrect = selectedAnswer == question.correctIndex
                                showFeedback = true
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            } else if (question.options.size == 2) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    AnswerButton(
                        text = question.options[0],
                        color = Color.Red,
                        onClick = {
                            selectedAnswer = 0
                            isCorrect = selectedAnswer == question.correctIndex
                            showFeedback = true
                        },
                        modifier = Modifier.weight(1f)
                    )
                    AnswerButton(
                        text = question.options[1],
                        color = Color.Blue,
                        onClick = {
                            selectedAnswer = 1
                            isCorrect = selectedAnswer == question.correctIndex
                            showFeedback = true
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun AnswerButton(text: String, color: Color, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .height(100.dp)
            .background(color, RoundedCornerShape(16.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, color = Color.White, fontSize = 18.sp)
    }
}

@Composable
fun FeedbackScreen(isCorrect: Boolean, onNext: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(2000)
        onNext()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (isCorrect) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("✔", color = Color.Green, fontSize = 48.sp)
                Text("Correct! You are in the podium")
            }
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("✘", color = Color.Red, fontSize = 48.sp)
                Text("Wrong! You are behind User56")
            }
        }
    }
}
