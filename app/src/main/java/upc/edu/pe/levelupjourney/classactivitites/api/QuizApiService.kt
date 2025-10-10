package upc.edu.pe.levelupjourney.classactivitites.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import upc.edu.pe.levelupjourney.classactivitites.domain.model.entities.QuizResponse

interface QuizApiService {
    
    @GET("api/v1/quizzes/my-quizzes")
    suspend fun getMyQuizzes(
        @Query("userId") userId: String,
        @Query("category") category: String? = null,
        @Query("search") search: String? = null,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): Response<QuizResponse>
}