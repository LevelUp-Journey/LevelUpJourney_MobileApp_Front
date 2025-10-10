package upc.edu.pe.levelupjourney.iam.api

import android.util.Log
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import upc.edu.pe.levelupjourney.iam.repositories.AuthRepository
import upc.edu.pe.levelupjourney.classactivitites.api.QuizApiService

object ApiClient {
    private const val BASE_URL = "http://192.168.0.119:8081" // For Android emulator pointing to localhost

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // This will be set when creating authenticated client
    private var authRepository: AuthRepository? = null

    private val authInterceptor = Interceptor { chain ->
        val request = chain.request()
        
        // Get token synchronously in IO context
        val token = runBlocking {
            authRepository?.getAccessToken()
        }
        
        Log.d("ApiClient", "=== AUTH INTERCEPTOR ===")
        Log.d("ApiClient", "Request URL: ${request.url}")
        Log.d("ApiClient", "Request Method: ${request.method}")
        Log.d("ApiClient", "Retrieved Token: $token")
        
        val newRequest = if (token != null) {
            Log.d("ApiClient", "Adding Authorization header with token")
            request.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            Log.w("ApiClient", "No token available, proceeding without Authorization header")
            request
        }
        
        Log.d("ApiClient", "Final request headers: ${newRequest.headers}")
        
        val response = chain.proceed(newRequest)
        Log.d("ApiClient", "Response code: ${response.code}")
        Log.d("ApiClient", "Response message: ${response.message}")
        
        response
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val authenticatedClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .build()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val authenticatedRetrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(authenticatedClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // API service instances
    val authApiService: AuthApiService by lazy {
        retrofit.create(AuthApiService::class.java)
    }

    val userApiService: UserApiService by lazy {
        authenticatedRetrofit.create(UserApiService::class.java)
    }

    val quizApiService: QuizApiService by lazy {
        authenticatedRetrofit.create(QuizApiService::class.java)
    }

    // Initialize with auth repository for token injection
    fun initialize(authRepo: AuthRepository) {
        authRepository = authRepo
    }
}