package upc.edu.pe.levelupjourney.classactivitites.api

import retrofit2.Response
import retrofit2.http.*
import upc.edu.pe.levelupjourney.classactivitites.domain.model.entities.Quiz
import upc.edu.pe.levelupjourney.classactivitites.domain.model.entities.QuizResponse
import upc.edu.pe.levelupjourney.classactivitites.domain.model.request.CreateQuizRequest
import upc.edu.pe.levelupjourney.classactivitites.domain.model.request.UpdateQuizRequest
import upc.edu.pe.levelupjourney.classactivitites.domain.model.request.CreateQuestionRequest
import upc.edu.pe.levelupjourney.classactivitites.domain.model.response.CreateQuizResponse
import upc.edu.pe.levelupjourney.classactivitites.domain.model.response.CreateQuestionResponse

interface QuizApiService {
    
    // ==================== QUIZ CRUD Operations ====================
    
    /**
     * Create a new quiz
     * POST /api/v1/quizzes
     */
    @POST("api/v1/quizzes")
    suspend fun createQuiz(
        @Body request: CreateQuizRequest
    ): Response<CreateQuizResponse>
    
    /**
     * Get a specific quiz by ID
     * GET /api/v1/quizzes/{quizId}
     */
    @GET("api/v1/quizzes/{quizId}")
    suspend fun getQuizById(
        @Path("quizId") quizId: Long,
        @Query("userId") userId: String,
        @Query("includeQuestions") includeQuestions: Boolean = true
    ): Response<Quiz>
    
    /**
     * Update an existing quiz
     * PUT /api/v1/quizzes/{quizId}
     */
    @PUT("api/v1/quizzes/{quizId}")
    suspend fun updateQuiz(
        @Path("quizId") quizId: Long,
        @Body request: UpdateQuizRequest
    ): Response<Unit>
    
    /**
     * Delete a quiz
     * DELETE /api/v1/quizzes/{quizId}
     */
    @DELETE("api/v1/quizzes/{quizId}")
    suspend fun deleteQuiz(
        @Path("quizId") quizId: Long,
        @Query("userId") userId: String
    ): Response<Unit>
    
    /**
     * Publish a quiz (change visibility from PRIVATE to PUBLIC)
     * POST /api/v1/quizzes/{quizId}/publish
     */
    @POST("api/v1/quizzes/{quizId}/publish")
    suspend fun publishQuiz(
        @Path("quizId") quizId: Long,
        @Query("userId") userId: String
    ): Response<Unit>
    
    // ==================== QUIZ QUERIES ====================
    
    /**
     * Get all public quizzes (paginated)
     * GET /api/v1/quizzes/public
     */
    @GET("api/v1/quizzes/public")
    suspend fun getPublicQuizzes(
        @Query("category") category: String? = null,
        @Query("search") search: String? = null,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): Response<QuizResponse>
    
    /**
     * Get my quizzes (created by current user)
     * GET /api/v1/quizzes/my-quizzes
     */
    @GET("api/v1/quizzes/my-quizzes")
    suspend fun getMyQuizzes(
        @Query("userId") userId: String,
        @Query("category") category: String? = null,
        @Query("search") search: String? = null,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): Response<QuizResponse>
    
    // ==================== QUESTION Operations ====================
    
    /**
     * Add a new question to a quiz
     * POST /api/v1/quizzes/{quizId}/questions
     */
    @POST("api/v1/quizzes/{quizId}/questions")
    suspend fun addQuestion(
        @Path("quizId") quizId: Long,
        @Body request: CreateQuestionRequest
    ): Response<CreateQuestionResponse>
    
    /**
     * Update an existing question
     * PUT /api/v1/quizzes/{quizId}/questions/{questionId}
     */
    @PUT("api/v1/quizzes/{quizId}/questions/{questionId}")
    suspend fun updateQuestion(
        @Path("quizId") quizId: Long,
        @Path("questionId") questionId: Long,
        @Body request: CreateQuestionRequest
    ): Response<Unit>
    
    /**
     * Delete a question from a quiz
     * DELETE /api/v1/quizzes/{quizId}/questions/{questionId}
     */
    @DELETE("api/v1/quizzes/{quizId}/questions/{questionId}")
    suspend fun deleteQuestion(
        @Path("quizId") quizId: Long,
        @Path("questionId") questionId: Long,
        @Query("userId") userId: String
    ): Response<Unit>
}
