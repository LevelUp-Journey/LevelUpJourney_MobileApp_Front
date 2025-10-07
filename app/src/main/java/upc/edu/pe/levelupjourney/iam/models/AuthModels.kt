package upc.edu.pe.levelupjourney.data.models

// Authentication request/response models
data class SignInRequest(
    val email_address: String,
    val password: String
)

data class SignUpRequest(
    val email_address: String,
    val password: String
)

data class AuthenticatedUser(
    val id: String,
    val email_address: String,
    val accessToken: String,
    val refreshToken: String
)

data class RefreshTokenRequest(
    val refreshToken: String
)

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String,
    val message: String
)

// User management models
data class User(
    val id: String,
    val email_address: String,
    val roles: List<String> = emptyList()
)

data class Role(
    val id: String,
    val name: String,
    val description: String? = null
)