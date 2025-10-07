package upc.edu.pe.levelupjourney.data.api

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import upc.edu.pe.levelupjourney.data.repositories.AuthRepository

object ApiClient {
    private const val BASE_URL = "http://192.168.0.231:8081" // For Android emulator pointing to localhost

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // This will be set when creating authenticated client
    private var authRepository: AuthRepository? = null

    private val authInterceptor = Interceptor { chain ->
        val token = authRepository?.let { repo ->
            runBlocking { repo.getAccessToken() }
        }
        val request = if (token != null) {
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            chain.request()
        }
        chain.proceed(request)
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

    // Initialize with auth repository for token injection
    fun initialize(authRepo: AuthRepository) {
        authRepository = authRepo
    }
}