package upc.edu.pe.levelupjourney.classactivitites.domain.model.request

import com.google.gson.annotations.SerializedName

data class CreateQuestionRequest(
    @SerializedName("questionText")
    val questionText: String,
    
    @SerializedName("questionType")
    val questionType: String, // MULTIPLE_CHOICE or TRUE_FALSE
    
    @SerializedName("timeLimit")
    val timeLimit: Int, // in seconds
    
    @SerializedName("points")
    val points: Int,
    
    @SerializedName("answers")
    val answers: List<String>,
    
    @SerializedName("correctAnswerIndex")
    val correctAnswerIndex: Int,
    
    @SerializedName("mediaUrl")
    val mediaUrl: String?,
    
    @SerializedName("userId")
    val userId: String
)
