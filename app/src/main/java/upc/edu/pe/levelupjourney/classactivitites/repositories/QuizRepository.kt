package upc.edu.pe.levelupjourney.classactivitites.repositories

import android.util.Log
import upc.edu.pe.levelupjourney.classactivitites.api.QuizApiService
import upc.edu.pe.levelupjourney.classactivitites.domain.model.entities.Quiz
import upc.edu.pe.levelupjourney.classactivitites.domain.model.request.CreateQuizRequest
import upc.edu.pe.levelupjourney.classactivitites.domain.model.request.UpdateQuizRequest
import upc.edu.pe.levelupjourney.classactivitites.domain.model.request.CreateQuestionRequest
import upc.edu.pe.levelupjourney.classactivitites.domain.model.response.CreateQuizResponse
import upc.edu.pe.levelupjourney.classactivitites.domain.model.response.CreateQuestionResponse

class QuizRepository(
    private val apiService: QuizApiService
) {
    
    // ==================== QUIZ CRUD Operations ====================
    
    suspend fun createQuiz(
        name: String,
        description: String?,
        category: String,
        coverImageUrl: String?,
        creatorId: String
    ): Result<CreateQuizResponse> {
        return try {
            Log.d("QuizRepository", "=== CREATING QUIZ ===")
            Log.d("QuizRepository", "Name: $name")
            
            val request = CreateQuizRequest(name, description, category, coverImageUrl, creatorId)
            val response = apiService.createQuiz(request)
            
            if (response.isSuccessful) {
                response.body()?.let {
                    Log.d("QuizRepository", "Quiz created with ID: ${it.id}")
                    Result.success(it)
                } ?: Result.failure(Exception("Response body is null"))
            } else {
                Result.failure(Exception("HTTP ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e("QuizRepository", "Create quiz exception", e)
            Result.failure(e)
        }
    }
    
    suspend fun getQuizById(
        quizId: Long,
        userId: String,
        includeQuestions: Boolean = true
    ): Result<Quiz> {
        return try {
            val response = apiService.getQuizById(quizId, userId, includeQuestions)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Response body is null"))
            } else {
                Result.failure(Exception("HTTP ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updateQuiz(
        quizId: Long,
        name: String,
        description: String?,
        category: String,
        coverImageUrl: String?,
        userId: String
    ): Result<Unit> {
        return try {
            val request = UpdateQuizRequest(name, description, category, coverImageUrl, userId)
            val response = apiService.updateQuiz(quizId, request)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("HTTP ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun deleteQuiz(quizId: Long, userId: String): Result<Unit> {
        return try {
            val response = apiService.deleteQuiz(quizId, userId)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("HTTP ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun publishQuiz(quizId: Long, userId: String): Result<Unit> {
        return try {
            val response = apiService.publishQuiz(quizId, userId)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("HTTP ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // ==================== QUIZ QUERIES ====================
    
    suspend fun getPublicQuizzes(
        category: String? = null,
        search: String? = null,
        page: Int = 0,
        size: Int = 20
    ): Result<List<Quiz>> {
        return try {
            val response = apiService.getPublicQuizzes(category, search, page, size)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it.content)
                } ?: Result.failure(Exception("Response body is null"))
            } else {
                Result.failure(Exception("HTTP ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getMyQuizzes(
        userId: String,
        category: String? = null,
        search: String? = null,
        page: Int = 0,
        size: Int = 20
    ): Result<List<Quiz>> {
        return try {
            Log.d("QuizRepository", "=== FETCHING MY QUIZZES ===")
            Log.d("QuizRepository", "User ID: $userId")
            
            val response = apiService.getMyQuizzes(userId, category, search, page, size)
            
            if (response.isSuccessful) {
                response.body()?.let {
                    Log.d("QuizRepository", "Total quizzes: ${it.content.size}")
                    Result.success(it.content)
                } ?: Result.failure(Exception("Response body is null"))
            } else {
                Log.e("QuizRepository", "Error: ${response.code()}")
                Result.failure(Exception("HTTP ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e("QuizRepository", "Exception", e)
            Result.failure(e)
        }
    }
    
    // ==================== QUESTION Operations ====================
    
    suspend fun addQuestion(
        quizId: Long,
        questionText: String,
        questionType: String,
        timeLimit: Int,
        points: Int,
        answers: List<String>,
        correctAnswerIndex: Int,
        mediaUrl: String?,
        userId: String
    ): Result<CreateQuestionResponse> {
        return try {
            val request = CreateQuestionRequest(
                questionText, questionType, timeLimit, points,
                answers, correctAnswerIndex, mediaUrl, userId
            )
            val response = apiService.addQuestion(quizId, request)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Response body is null"))
            } else {
                Result.failure(Exception("HTTP ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updateQuestion(
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
    ): Result<Unit> {
        return try {
            val request = CreateQuestionRequest(
                questionText, questionType, timeLimit, points,
                answers, correctAnswerIndex, mediaUrl, userId
            )
            val response = apiService.updateQuestion(quizId, questionId, request)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("HTTP ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun deleteQuestion(
        quizId: Long,
        questionId: Long,
        userId: String
    ): Result<Unit> {
        return try {
            val response = apiService.deleteQuestion(quizId, questionId, userId)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("HTTP ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
