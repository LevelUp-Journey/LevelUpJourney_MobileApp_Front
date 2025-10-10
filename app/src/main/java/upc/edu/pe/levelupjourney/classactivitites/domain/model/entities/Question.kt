package upc.edu.pe.levelupjourney.classactivitites.domain.model.entities

import com.google.gson.annotations.SerializedName

data class Question(
    @SerializedName("id")
    val id: Long,
    
    @SerializedName("content")
    val content: String,
    
    @SerializedName("contentType")
    val contentType: String, // TEXT or IMAGE
    
    @SerializedName("questionType")
    val questionType: String, // MULTIPLE_CHOICE or TRUE_FALSE
    
    @SerializedName("points")
    val points: Int,
    
    @SerializedName("timeLimitSeconds")
    val timeLimitSeconds: Int,
    
    @SerializedName("questionOrder")
    val questionOrder: Int,
    
    @SerializedName("answers")
    val answers: List<Answer> = emptyList(),
    
    @SerializedName("createdAt")
    val createdAt: String,
    
    @SerializedName("updatedAt")
    val updatedAt: String
)
