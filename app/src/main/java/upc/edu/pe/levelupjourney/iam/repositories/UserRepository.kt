package upc.edu.pe.levelupjourney.data.repositories

import upc.edu.pe.levelupjourney.data.api.UserApiService
import upc.edu.pe.levelupjourney.data.models.Role
import upc.edu.pe.levelupjourney.data.models.User

class UserRepository(private val apiService: UserApiService) {

    suspend fun getAllUsers(): Result<List<User>> {
        return try {
            val response = apiService.getAllUsers()
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) } ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("Failed to get users: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserById(userId: String): Result<User> {
        return try {
            val response = apiService.getUserById(userId)
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) } ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("Failed to get user: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllRoles(): Result<List<Role>> {
        return try {
            val response = apiService.getAllRoles()
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) } ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("Failed to get roles: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}