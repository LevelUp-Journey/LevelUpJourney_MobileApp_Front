package upc.edu.pe.levelupjourney.classactivitites.repositories

import android.util.Log
import upc.edu.pe.levelupjourney.classactivitites.api.QuizApiService
import upc.edu.pe.levelupjourney.classactivitites.domain.model.entities.Quiz

class QuizRepository(
    private val apiService: QuizApiService
) {
    
    suspend fun getMyQuizzes(
        userId: String,
        category: String? = null,
        search: String? = null,
        page: Int = 0,
        size: Int = 20
    ): Result<List<Quiz>> {
        return try {
            Log.d("QuizRepository", "Fetching quizzes for user: $userId")
            val response = apiService.getMyQuizzes(userId, category, search, page, size)
            
            if (response.isSuccessful) {
                val quizResponse = response.body()
                if (quizResponse != null) {
                    Log.d("QuizRepository", "Successfully fetched ${quizResponse.content.size} quizzes")
                    Result.success(quizResponse.content)
                } else {
                    Log.e("QuizRepository", "Response body is null")
                    Result.failure(Exception("Response body is null"))
                }
            } else {
                val errorMsg = "HTTP ${response.code()}: ${response.message()}"
                Log.e("QuizRepository", "API Error: $errorMsg")
                Log.e("QuizRepository", "Error body: ${response.errorBody()?.string()}")
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Log.e("QuizRepository", "Network error fetching quizzes", e)
            Result.failure(e)
        }
    }
}