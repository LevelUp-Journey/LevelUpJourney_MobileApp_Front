package upc.edu.pe.levelupjourney.iam.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import upc.edu.pe.levelupjourney.iam.models.User
import upc.edu.pe.levelupjourney.iam.models.Role

interface UserApiService {
    @GET("/api/v1/users")
    suspend fun getAllUsers(): Response<List<User>>

    @GET("/api/v1/users/{userId}")
    suspend fun getUserById(@Path("userId") userId: String): Response<User>

    @GET("/api/v1/roles")
    suspend fun getAllRoles(): Response<List<Role>>
}