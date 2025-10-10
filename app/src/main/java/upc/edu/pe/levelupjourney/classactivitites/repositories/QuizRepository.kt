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
            Log.d("QuizRepository", "=== FETCHING MY QUIZZES ===")
            Log.d("QuizRepository", "User ID: $userId")
            Log.d("QuizRepository", "Category: $category")
            Log.d("QuizRepository", "Search: $search")
            Log.d("QuizRepository", "Page: $page")
            Log.d("QuizRepository", "Size: $size")
            
            val response = apiService.getMyQuizzes(userId, category, search, page, size)
            
            Log.d("QuizRepository", "=== QUIZ API RESPONSE ===")
            Log.d("QuizRepository", "Response Code: ${response.code()}")
            Log.d("QuizRepository", "Response Message: ${response.message()}")
            Log.d("QuizRepository", "Response Headers: ${response.headers()}")
            
            if (response.isSuccessful) {
                val quizResponse = response.body()
                if (quizResponse != null) {
                    Log.d("QuizRepository", "=== QUIZ FETCH SUCCESS ===")
                    Log.d("QuizRepository", "Total quizzes: ${quizResponse.content.size}")
                    Log.d("QuizRepository", "Total pages: ${quizResponse.totalPages}")
                    Log.d("QuizRepository", "Total elements: ${quizResponse.totalElements}")
                    
                    quizResponse.content.forEachIndexed { index, quiz ->
                        Log.d("QuizRepository", "Quiz $index: ${quiz.name} (ID: ${quiz.id})")
                    }
                    
                    Result.success(quizResponse.content)
                } else {
                    Log.e("QuizRepository", "Response body is null despite successful response")
                    Result.failure(Exception("Response body is null"))
                }
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("QuizRepository", "=== QUIZ FETCH ERROR ===")
                Log.e("QuizRepository", "Error Code: ${response.code()}")
                Log.e("QuizRepository", "Error Message: ${response.message()}")
                Log.e("QuizRepository", "Error Body: $errorBody")
                Result.failure(Exception("HTTP ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e("QuizRepository", "=== QUIZ FETCH EXCEPTION ===", e)
            Log.e("QuizRepository", "Exception type: ${e.javaClass.simpleName}")
            Log.e("QuizRepository", "Exception message: ${e.message}")
            Result.failure(e)
        }
    }
}