package upc.edu.pe.levelupjourney.classactivitites.domain.model.entities

import com.google.gson.annotations.SerializedName

data class Answer(
    @SerializedName("id")
    val id: Long,
    
    @SerializedName("content")
    val content: String,
    
    @SerializedName("contentType")
    val contentType: String, // TEXT or IMAGE
    
    @SerializedName("isCorrect")
    val isCorrect: Boolean,
    
    @SerializedName("createdAt")
    val createdAt: String,
    
    @SerializedName("updatedAt")
    val updatedAt: String
)
