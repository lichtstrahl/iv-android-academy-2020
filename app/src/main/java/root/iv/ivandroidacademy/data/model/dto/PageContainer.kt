package root.iv.ivandroidacademy.data.model.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PageContainer<T>(
    @SerialName("page")
    val page: Int,
    @SerialName("results")
    val data: List<T>,
    @SerialName("total_results")
    val total: Int,
    @SerialName("total_pages")
    val pages: Int
)
