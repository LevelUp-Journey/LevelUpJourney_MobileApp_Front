package upc.edu.pe.levelupjourney.data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import upc.edu.pe.levelupjourney.data.models.*

interface AuthApiService {
    @POST("/api/v1/authentication/sign-in")
    suspend fun signIn(@Body request: SignInRequest): Response<AuthenticatedUser>

    @POST("/api/v1/authentication/sign-up")
    suspend fun signUp(@Body request: SignUpRequest): Response<AuthenticatedUser>

    @POST("/api/v1/authentication/refresh")
    suspend fun refreshToken(@Body request: RefreshTokenRequest): Response<TokenResponse>
}