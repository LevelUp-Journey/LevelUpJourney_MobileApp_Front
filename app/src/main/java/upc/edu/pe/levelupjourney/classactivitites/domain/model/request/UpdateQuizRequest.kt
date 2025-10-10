package upc.edu.pe.levelupjourney.classactivitites.domain.model.request

import com.google.gson.annotations.SerializedName

data class UpdateQuizRequest(
    @SerializedName("name")
    val name: String,
    
    @SerializedName("description")
    val description: String?,
    
    @SerializedName("category")
    val category: String,
    
    @SerializedName("coverImageUrl")
    val coverImageUrl: String?,
    
    @SerializedName("userId")
    val userId: String
)
