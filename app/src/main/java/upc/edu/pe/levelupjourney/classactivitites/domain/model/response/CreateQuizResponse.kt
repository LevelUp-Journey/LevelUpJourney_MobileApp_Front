package upc.edu.pe.levelupjourney.classactivitites.domain.model.response

import com.google.gson.annotations.SerializedName

data class CreateQuizResponse(
    @SerializedName("id")
    val id: Long,
    
    @SerializedName("message")
    val message: String
)
