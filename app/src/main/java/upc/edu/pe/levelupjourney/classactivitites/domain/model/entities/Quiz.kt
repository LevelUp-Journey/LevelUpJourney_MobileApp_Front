package upc.edu.pe.levelupjourney.classactivitites.domain.model.entities

import com.google.gson.annotations.SerializedName

data class Quiz(
    @SerializedName("id")
    val id: Long,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("description")
    val description: String?,
    
    @SerializedName("category")
    val category: String,
    
    @SerializedName("coverImageUrl")
    val coverImageUrl: String?,
    
    @SerializedName("visibility")
    val visibility: String,
    
    @SerializedName("creatorId")
    val creatorId: CreatorId,
    
    @SerializedName("createdAt")
    val createdAt: String,
    
    @SerializedName("updatedAt")
    val updatedAt: String,
    
    @SerializedName("questions")
    val questions: List<Any> = emptyList()
)

data class CreatorId(
    @SerializedName("value")
    val value: String
)

data class QuizResponse(
    @SerializedName("content")
    val content: List<Quiz>,
    
    @SerializedName("pageable")
    val pageable: Pageable,
    
    @SerializedName("totalPages")
    val totalPages: Int,
    
    @SerializedName("totalElements")
    val totalElements: Long,
    
    @SerializedName("last")
    val last: Boolean,
    
    @SerializedName("first")
    val first: Boolean,
    
    @SerializedName("numberOfElements")
    val numberOfElements: Int,
    
    @SerializedName("size")
    val size: Int,
    
    @SerializedName("number")
    val number: Int,
    
    @SerializedName("empty")
    val empty: Boolean
)

data class Pageable(
    @SerializedName("page")
    val page: Int,
    
    @SerializedName("size")
    val size: Int,
    
    @SerializedName("offset")
    val offset: Long,
    
    @SerializedName("paged")
    val paged: Boolean,
    
    @SerializedName("unpaged")
    val unpaged: Boolean
)