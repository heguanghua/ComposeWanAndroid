package com.compose.wan.android.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HomeArticleListResp (
    @SerialName("total") val total: Int = 0,

    @SerialName("size") val size: Int = 0,

    @SerialName("pageCount") val pageCount: String = "",

    @SerialName("datas") val datas: List<SingleHomeArticle> = emptyList(),
)