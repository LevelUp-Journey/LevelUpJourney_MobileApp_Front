package upc.edu.pe.levelupjourney.community.domain.model

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class Post(
    val id: Int,
    val user: String,
    val description: String,
    comments: Int = 0,
    likes: Int = 0
) {
    var liked by mutableStateOf(false)
    var likes by mutableStateOf(likes)
    var comments by mutableStateOf(comments)
}

data class Comment(
    val id: Int,
    val user: String,
    val text: String
)
