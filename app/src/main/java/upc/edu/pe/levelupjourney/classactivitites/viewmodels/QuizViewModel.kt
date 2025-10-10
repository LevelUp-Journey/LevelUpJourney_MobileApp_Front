package upc.edu.pe.levelupjourney.classactivitites.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import upc.edu.pe.levelupjourney.classactivitites.domain.model.entities.Quiz
import upc.edu.pe.levelupjourney.classactivitites.repositories.QuizRepository

class QuizViewModel(
    private val quizRepository: QuizRepository
) : ViewModel() {
    
    private val _quizState = MutableStateFlow<QuizState>(QuizState.Idle)
    val quizState: StateFlow<QuizState> = _quizState
    
    private val _quizzes = MutableStateFlow<List<Quiz>>(emptyList())
    val quizzes: StateFlow<List<Quiz>> = _quizzes
    
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
                    _quizState.value = QuizState.Error(error.message ?: "Unknown error occurred")
                }
            )
        }
    }
    
    fun resetState() {
        _quizState.value = QuizState.Idle
    }
}

sealed class QuizState {
    object Idle : QuizState()
    object Loading : QuizState()
    data class Success(val quizzes: List<Quiz>) : QuizState()
    data class Error(val message: String) : QuizState()
}