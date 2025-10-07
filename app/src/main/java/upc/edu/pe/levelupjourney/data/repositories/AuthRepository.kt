package upc.edu.pe.levelupjourney.data.repositories

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import upc.edu.pe.levelupjourney.data.api.AuthApiService
import upc.edu.pe.levelupjourney.data.models.AuthenticatedUser
import upc.edu.pe.levelupjourney.data.models.RefreshTokenRequest
import upc.edu.pe.levelupjourney.data.models.SignInRequest
import upc.edu.pe.levelupjourney.data.models.SignUpRequest
import upc.edu.pe.levelupjourney.data.models.TokenResponse

private val Context.dataStore by preferencesDataStore("auth_prefs")

class AuthRepository(
    private val context: Context,
    private val apiService: AuthApiService
) {
    private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
    private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
    private val USER_ID_KEY = stringPreferencesKey("user_id")
    private val USER_EMAIL_KEY = stringPreferencesKey("user_email")

    suspend fun signIn(email: String, password: String): Result<AuthenticatedUser> {
        return try {
            val response = apiService.signIn(SignInRequest(email, password))
            if (response.isSuccessful) {
                response.body()?.let { user ->
                    saveUserData(user)
                    Result.success(user)
                } ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("Sign-in failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signUp(email: String, password: String): Result<AuthenticatedUser> {
        return try {
            val response = apiService.signUp(SignUpRequest(email, password))
            if (response.isSuccessful) {
                response.body()?.let { user ->
                    saveUserData(user)
                    Result.success(user)
                } ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("Sign-up failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun refreshToken(): Result<TokenResponse> {
        val refreshToken = getRefreshToken() ?: return Result.failure(Exception("No refresh token available"))
        return try {
            val response = apiService.refreshToken(RefreshTokenRequest(refreshToken))
            if (response.isSuccessful) {
                response.body()?.let { tokenResponse ->
                    updateTokens(tokenResponse.accessToken, tokenResponse.refreshToken)
                    Result.success(tokenResponse)
                } ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("Token refresh failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun saveUserData(user: AuthenticatedUser) {
        context.dataStore.edit { prefs ->
            prefs[ACCESS_TOKEN_KEY] = user.accessToken
            prefs[REFRESH_TOKEN_KEY] = user.refreshToken
            prefs[USER_ID_KEY] = user.id
            prefs[USER_EMAIL_KEY] = user.email_address
        }
    }

    private suspend fun updateTokens(accessToken: String, refreshToken: String) {
        context.dataStore.edit { prefs ->
            prefs[ACCESS_TOKEN_KEY] = accessToken
            prefs[REFRESH_TOKEN_KEY] = refreshToken
        }
    }

    suspend fun getAccessToken(): String? {
        return context.dataStore.data.map { prefs ->
            prefs[ACCESS_TOKEN_KEY]
        }.first()
    }

    private suspend fun getRefreshToken(): String? {
        return context.dataStore.data.map { prefs ->
            prefs[REFRESH_TOKEN_KEY]
        }.first()
    }

    suspend fun getCurrentUserId(): String? {
        return context.dataStore.data.map { prefs ->
            prefs[USER_ID_KEY]
        }.first()
    }

    suspend fun getCurrentUserEmail(): String? {
        return context.dataStore.data.map { prefs ->
            prefs[USER_EMAIL_KEY]
        }.first()
    }

    suspend fun isUserLoggedIn(): Boolean {
        return getAccessToken() != null
    }

    suspend fun logout() {
        context.dataStore.edit { prefs ->
            prefs.remove(ACCESS_TOKEN_KEY)
            prefs.remove(REFRESH_TOKEN_KEY)
            prefs.remove(USER_ID_KEY)
            prefs.remove(USER_EMAIL_KEY)
        }
    }
}