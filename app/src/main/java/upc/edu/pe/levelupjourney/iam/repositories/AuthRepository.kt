package upc.edu.pe.levelupjourney.iam.repositories

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import upc.edu.pe.levelupjourney.iam.api.AuthApiService
import upc.edu.pe.levelupjourney.iam.models.AuthenticatedUser
import upc.edu.pe.levelupjourney.iam.models.RefreshTokenRequest
import upc.edu.pe.levelupjourney.iam.models.SignInRequest
import upc.edu.pe.levelupjourney.iam.models.SignUpRequest
import upc.edu.pe.levelupjourney.iam.models.TokenResponse

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
            Log.d("AuthRepository", "=== STARTING SIGN IN PROCESS ===")
            Log.d("AuthRepository", "Email: $email")
            Log.d("AuthRepository", "Password: ${password.take(3)}***")
            
            val request = SignInRequest(email, password)
            Log.d("AuthRepository", "Created SignInRequest: $request")
            
            val response = apiService.signIn(request)
            Log.d("AuthRepository", "API Response Code: ${response.code()}")
            Log.d("AuthRepository", "API Response Message: ${response.message()}")
            
            if (response.isSuccessful) {
                response.body()?.let { user ->
                    Log.d("AuthRepository", "=== SIGN IN SUCCESSFUL ===")
                    Log.d("AuthRepository", "User ID: ${user.id}")
                    Log.d("AuthRepository", "User Email: ${user.email}")
                    Log.d("AuthRepository", "Access Token: ${user.token.take(20)}...")
                    Log.d("AuthRepository", "Refresh Token: ${user.refreshToken.take(20)}...")
                    
                    saveUserData(user)
                    Log.d("AuthRepository", "User data saved to DataStore")
                    Result.success(user)
                } ?: run {
                    Log.e("AuthRepository", "Response body is null despite successful response")
                    Result.failure(Exception("Empty response"))
                }
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("AuthRepository", "=== SIGN IN FAILED ===")
                Log.e("AuthRepository", "Error Code: ${response.code()}")
                Log.e("AuthRepository", "Error Message: ${response.message()}")
                Log.e("AuthRepository", "Error Body: $errorBody")
                Result.failure(Exception("Sign-in failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "=== SIGN IN EXCEPTION ===", e)
            Log.e("AuthRepository", "Exception type: ${e.javaClass.simpleName}")
            Log.e("AuthRepository", "Exception message: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun signUp(email: String, password: String): Result<AuthenticatedUser> {
        return try {
            Log.d("AuthRepository", "=== STARTING SIGN UP PROCESS ===")
            Log.d("AuthRepository", "Email: $email")
            
            val request = SignUpRequest(email, password)
            val response = apiService.signUp(request)
            
            Log.d("AuthRepository", "API Response Code: ${response.code()}")
            
            if (response.isSuccessful) {
                response.body()?.let { signUpResponse ->
                    Log.d("AuthRepository", "=== SIGN UP SUCCESSFUL ===")
                    Log.d("AuthRepository", "User ID: ${signUpResponse.id}")
                    Log.d("AuthRepository", "Username: ${signUpResponse.username}")
                    Log.d("AuthRepository", "Note: Sign up successful, but user needs to sign in to get token")
                    
                    // SignUp no retorna token, entonces retornamos un error indicando que debe hacer sign-in
                    // O mejor aún, hacemos sign-in automáticamente
                    Log.d("AuthRepository", "Performing automatic sign-in after sign-up...")
                    return signIn(email, password)
                } ?: run {
                    Log.e("AuthRepository", "Response body is null despite successful response")
                    Result.failure(Exception("Empty response"))
                }
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("AuthRepository", "=== SIGN UP FAILED ===")
                Log.e("AuthRepository", "Error Code: ${response.code()}")
                Log.e("AuthRepository", "Error Message: ${response.message()}")
                Log.e("AuthRepository", "Error Body: $errorBody")
                Result.failure(Exception("Sign-up failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "=== SIGN UP EXCEPTION ===", e)
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
        Log.d("AuthRepository", "=== SAVING USER DATA ===")
        Log.d("AuthRepository", "Saving Access Token: ${user.token.take(20)}...")
        Log.d("AuthRepository", "Saving Refresh Token: ${user.refreshToken.take(20)}...")
        Log.d("AuthRepository", "Saving User ID: ${user.id}")
        Log.d("AuthRepository", "Saving User Email: ${user.email}")
        
        context.dataStore.edit { prefs ->
            prefs[ACCESS_TOKEN_KEY] = user.token
            prefs[REFRESH_TOKEN_KEY] = user.refreshToken
            prefs[USER_ID_KEY] = user.id
            prefs[USER_EMAIL_KEY] = user.email
        }
        
        Log.d("AuthRepository", "User data saved successfully to DataStore")
        
        // Verify saved data
        val savedToken = getAccessToken()
        Log.d("AuthRepository", "Verification - Saved token: $savedToken")
    }

    private suspend fun updateTokens(accessToken: String, refreshToken: String) {
        context.dataStore.edit { prefs ->
            prefs[ACCESS_TOKEN_KEY] = accessToken
            prefs[REFRESH_TOKEN_KEY] = refreshToken
        }
    }

    suspend fun getAccessToken(): String? {
        val token = context.dataStore.data.map { prefs ->
            prefs[ACCESS_TOKEN_KEY]
        }.first()
        Log.d("AuthRepository", "Retrieved access token: $token")
        return token
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