package upc.edu.pe.levelupjourney.classactivitites.domain.model.response

import com.google.gson.annotations.SerializedName

data class CreateQuestionResponse(
    @SerializedName("id")
    val id: Long,
    
    @SerializedName("message")
    val message: String
)
