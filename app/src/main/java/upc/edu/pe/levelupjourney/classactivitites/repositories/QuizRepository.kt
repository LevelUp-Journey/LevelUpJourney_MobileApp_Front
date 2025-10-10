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
    // Helper to unify network call handling and reduce duplication
    private suspend fun <T> safeApiCall(call: suspend () -> retrofit2.Response<T>): Result<T> {
        return try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(body)
                } else {
                    Result.failure(Exception("Empty response body"))
                }
            } else {
                val err = response.errorBody()?.string()
                Result.failure(Exception("HTTP ${response.code()}: ${response.message()} - $err"))
            }
        } catch (e: Exception) {
            Log.e("QuizRepository", "Network call failed", e)
            Result.failure(e)
        }
    }

    // Helper for endpoints that return no body (Unit) - treat successful HTTP status as success
    private suspend fun safeApiCallUnit(call: suspend () -> retrofit2.Response<*>): Result<Unit> {
        return try {
            val response = call()
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                val err = response.errorBody()?.string()
                Result.failure(Exception("HTTP ${response.code()}: ${response.message()} - $err"))
            }
        } catch (e: Exception) {
            Log.e("QuizRepository", "Network call failed", e)
            Result.failure(e)
        }
    }

    // ==================== QUIZ CRUD Operations ====================

    suspend fun createQuiz(
        name: String,
        description: String?,
        category: String,
        coverImageUrl: String?,
        creatorId: String
    ): Result<CreateQuizResponse> {
        Log.d("QuizRepository", "Creating quiz: $name")
        val request = CreateQuizRequest(name, description, category, coverImageUrl, creatorId)
        return safeApiCall { apiService.createQuiz(request) }
    }

    suspend fun getQuizById(
        quizId: Long,
        userId: String,
        includeQuestions: Boolean = true
    ): Result<Quiz> {
        return safeApiCall { apiService.getQuizById(quizId, userId, includeQuestions) }
    }

    suspend fun updateQuiz(
        quizId: Long,
        name: String,
        description: String?,
        category: String,
        coverImageUrl: String?,
        userId: String
    ): Result<Unit> {
        val request = UpdateQuizRequest(name, description, category, coverImageUrl, userId)
        // Use safeApiCallUnit because the endpoint returns no body
        return safeApiCallUnit { apiService.updateQuiz(quizId, request) }
    }

    suspend fun deleteQuiz(quizId: Long, userId: String): Result<Unit> {
        return safeApiCallUnit { apiService.deleteQuiz(quizId, userId) }
    }

    suspend fun publishQuiz(quizId: Long, userId: String): Result<Unit> {
        return safeApiCallUnit { apiService.publishQuiz(quizId, userId) }
    }

    // ==================== QUIZ QUERIES ====================

    suspend fun getPublicQuizzes(
        category: String? = null,
        search: String? = null,
        page: Int = 0,
        size: Int = 20
    ): Result<List<Quiz>> {
        val res = safeApiCall { apiService.getPublicQuizzes(category, search, page, size) }
        return if (res.isSuccess) {
            Result.success(res.getOrNull()!!.content)
        } else {
            Result.failure(res.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }

    suspend fun getMyQuizzes(
        userId: String,
        category: String? = null,
        search: String? = null,
        page: Int = 0,
        size: Int = 20
    ): Result<List<Quiz>> {
        Log.d("QuizRepository", "Fetching quizzes for user: $userId")
        val res = safeApiCall { apiService.getMyQuizzes(userId, category, search, page, size) }
        return if (res.isSuccess) {
            val list = res.getOrNull()!!.content
            Log.d("QuizRepository", "Fetched ${list.size} quizzes")
            Result.success(list)
        } else {
            Result.failure(res.exceptionOrNull() ?: Exception("Unknown error"))
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
        val request = CreateQuestionRequest(
            questionText, questionType, timeLimit, points,
            answers, correctAnswerIndex, mediaUrl, userId
        )
        return safeApiCall { apiService.addQuestion(quizId, request) }
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
        val request = CreateQuestionRequest(
            questionText, questionType, timeLimit, points,
            answers, correctAnswerIndex, mediaUrl, userId
        )
        return safeApiCallUnit { apiService.updateQuestion(quizId, questionId, request) }
    }

    suspend fun deleteQuestion(
        quizId: Long,
        questionId: Long,
        userId: String
    ): Result<Unit> {
        return safeApiCallUnit { apiService.deleteQuestion(quizId, questionId, userId) }
    }
}
