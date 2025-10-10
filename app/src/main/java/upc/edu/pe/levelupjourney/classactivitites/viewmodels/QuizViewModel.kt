package upc.edu.pe.levelupjourney.classactivitites.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import upc.edu.pe.levelupjourney.classactivitites.domain.model.entities.Quiz
import upc.edu.pe.levelupjourney.classactivitites.repositories.QuizRepository
import upc.edu.pe.levelupjourney.classactivitites.domain.model.response.CreateQuizResponse
import upc.edu.pe.levelupjourney.classactivitites.domain.model.response.CreateQuestionResponse

class QuizViewModel(
    private val quizRepository: QuizRepository
) : ViewModel() {
    
    // State for quiz list operations
    private val _quizState = MutableStateFlow<QuizState>(QuizState.Idle)
    val quizState: StateFlow<QuizState> = _quizState
    
    private val _quizzes = MutableStateFlow<List<Quiz>>(emptyList())
    val quizzes: StateFlow<List<Quiz>> = _quizzes
    
    // State for single quiz operations
    private val _currentQuiz = MutableStateFlow<Quiz?>(null)
    val currentQuiz: StateFlow<Quiz?> = _currentQuiz
    
    // State for quiz creation
    private val _createQuizState = MutableStateFlow<CreateQuizState>(CreateQuizState.Idle)
    val createQuizState: StateFlow<CreateQuizState> = _createQuizState
    
    // State for quiz update/delete operations
    private val _operationState = MutableStateFlow<OperationState>(OperationState.Idle)
    val operationState: StateFlow<OperationState> = _operationState
    
    // State for question operations
    private val _questionState = MutableStateFlow<QuestionState>(QuestionState.Idle)
    val questionState: StateFlow<QuestionState> = _questionState
    
    // ==================== QUIZ CRUD Operations ====================
    
    fun createQuiz(
        name: String,
        description: String?,
        category: String,
        coverImageUrl: String?,
        creatorId: String
    ) {
        viewModelScope.launch {
            _createQuizState.value = CreateQuizState.Loading
            
            val result = quizRepository.createQuiz(name, description, category, coverImageUrl, creatorId)
            
            result.fold(
                onSuccess = { response ->
                    _createQuizState.value = CreateQuizState.Success(response)
                },
                onFailure = { error ->
                    _createQuizState.value = CreateQuizState.Error(error.message ?: "Failed to create quiz")
                }
            )
        }
    }
    
    fun getQuizById(quizId: Long, userId: String, includeQuestions: Boolean = true) {
        viewModelScope.launch {
            _quizState.value = QuizState.Loading
            
            val result = quizRepository.getQuizById(quizId, userId, includeQuestions)
            
            result.fold(
                onSuccess = { quiz ->
                    _currentQuiz.value = quiz
                    _quizState.value = QuizState.Success(listOf(quiz))
                },
                onFailure = { error ->
                    _quizState.value = QuizState.Error(error.message ?: "Failed to fetch quiz")
                }
            )
        }
    }
    
    fun updateQuiz(
        quizId: Long,
        name: String,
        description: String?,
        category: String,
        coverImageUrl: String?,
        userId: String
    ) {
        viewModelScope.launch {
            _operationState.value = OperationState.Loading
            
            val result = quizRepository.updateQuiz(quizId, name, description, category, coverImageUrl, userId)
            
            result.fold(
                onSuccess = {
                    _operationState.value = OperationState.Success("Quiz updated successfully")
                },
                onFailure = { error ->
                    _operationState.value = OperationState.Error(error.message ?: "Failed to update quiz")
                }
            )
        }
    }
    
    fun deleteQuiz(quizId: Long, userId: String) {
        viewModelScope.launch {
            _operationState.value = OperationState.Loading
            
            val result = quizRepository.deleteQuiz(quizId, userId)
            
            result.fold(
                onSuccess = {
                    _operationState.value = OperationState.Success("Quiz deleted successfully")
                    // Remove from local list if exists
                    _quizzes.value = _quizzes.value.filter { it.id != quizId }
                },
                onFailure = { error ->
                    _operationState.value = OperationState.Error(error.message ?: "Failed to delete quiz")
                }
            )
        }
    }
    
    fun publishQuiz(quizId: Long, userId: String) {
        viewModelScope.launch {
            _operationState.value = OperationState.Loading
            
            val result = quizRepository.publishQuiz(quizId, userId)
            
            result.fold(
                onSuccess = {
                    _operationState.value = OperationState.Success("Quiz published successfully")
                },
                onFailure = { error ->
                    _operationState.value = OperationState.Error(error.message ?: "Failed to publish quiz")
                }
            )
        }
    }
    
    // ==================== QUIZ QUERIES ====================
    
    fun fetchPublicQuizzes(
        category: String? = null,
        search: String? = null,
        page: Int = 0,
        size: Int = 20
    ) {
        viewModelScope.launch {
            _quizState.value = QuizState.Loading
            
            val result = quizRepository.getPublicQuizzes(category, search, page, size)
            
            result.fold(
                onSuccess = { quizList ->
                    _quizzes.value = quizList
                    _quizState.value = QuizState.Success(quizList)
                },
                onFailure = { error ->
                    _quizState.value = QuizState.Error(error.message ?: "Failed to fetch public quizzes")
                }
            )
        }
    }
    
    fun fetchMyQuizzes(
        userId: String,
        category: String? = null,
        search: String? = null,
        page: Int = 0,
        size: Int = 20
    ) {
        viewModelScope.launch {
            _quizState.value = QuizState.Loading
            
            val result = quizRepository.getMyQuizzes(userId, category, search, page, size)
            
            result.fold(
                onSuccess = { quizList ->
                    _quizzes.value = quizList
                    _quizState.value = QuizState.Success(quizList)
                },
                onFailure = { error ->
                    _quizState.value = QuizState.Error(error.message ?: "Failed to fetch my quizzes")
                }
            )
        }
    }
    
    // ==================== QUESTION Operations ====================
    
    fun addQuestion(
        quizId: Long,
        questionText: String,
        questionType: String,
        timeLimit: Int,
        points: Int,
        answers: List<String>,
        correctAnswerIndex: Int,
        mediaUrl: String?,
        userId: String
    ) {
        viewModelScope.launch {
            _questionState.value = QuestionState.Loading
            
            val result = quizRepository.addQuestion(
                quizId, questionText, questionType, timeLimit,
                points, answers, correctAnswerIndex, mediaUrl, userId
            )
            
            result.fold(
                onSuccess = { response ->
                    _questionState.value = QuestionState.Success(response.message)
                },
                onFailure = { error ->
                    _questionState.value = QuestionState.Error(error.message ?: "Failed to add question")
                }
            )
        }
    }
    
    fun updateQuestion(
        quizId: Long,
        questionId: Long,
        questionText: String,
        questionType: String,
        timeLimit: Int,
        points: Int,
        answers: List<String>,
        correctAnswerIndex: Int,
        mediaUrl: String?,
        userId: String
    ) {
        viewModelScope.launch {
            _questionState.value = QuestionState.Loading
            
            val result = quizRepository.updateQuestion(
                quizId, questionId, questionText, questionType,
                timeLimit, points, answers, correctAnswerIndex, mediaUrl, userId
            )
            
            result.fold(
                onSuccess = {
                    _questionState.value = QuestionState.Success("Question updated successfully")
                },
                onFailure = { error ->
                    _questionState.value = QuestionState.Error(error.message ?: "Failed to update question")
                }
            )
        }
    }
    
    fun deleteQuestion(quizId: Long, questionId: Long, userId: String) {
        viewModelScope.launch {
            _questionState.value = QuestionState.Loading
            
            val result = quizRepository.deleteQuestion(quizId, questionId, userId)
            
            result.fold(
                onSuccess = {
                    _questionState.value = QuestionState.Success("Question deleted successfully")
                },
                onFailure = { error ->
                    _questionState.value = QuestionState.Error(error.message ?: "Failed to delete question")
                }
            )
        }
    }
    
    // ==================== State Reset ====================
    
    fun resetQuizState() {
        _quizState.value = QuizState.Idle
    }
    
    fun resetCreateQuizState() {
        _createQuizState.value = CreateQuizState.Idle
    }
    
    fun resetOperationState() {
        _operationState.value = OperationState.Idle
    }
    
    fun resetQuestionState() {
        _questionState.value = QuestionState.Idle
    }
    
    fun clearCurrentQuiz() {
        _currentQuiz.value = null
    }
}

// ==================== State Classes ====================

sealed class QuizState {
    object Idle : QuizState()
    object Loading : QuizState()
    data class Success(val quizzes: List<Quiz>) : QuizState()
    data class Error(val message: String) : QuizState()
}

sealed class CreateQuizState {
    object Idle : CreateQuizState()
    object Loading : CreateQuizState()
    data class Success(val response: CreateQuizResponse) : CreateQuizState()
    data class Error(val message: String) : CreateQuizState()
}

sealed class OperationState {
    object Idle : OperationState()
    object Loading : OperationState()
    data class Success(val message: String) : OperationState()
    data class Error(val message: String) : OperationState()
}

sealed class QuestionState {
    object Idle : QuestionState()
    object Loading : QuestionState()
    data class Success(val message: String) : QuestionState()
    data class Error(val message: String) : QuestionState()
}
