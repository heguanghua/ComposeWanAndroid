package com.compose.wan.android.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SingleHomeArticle (
    @SerialName("title") val title: String,

    @SerialName("author") val author: String?,

    @SerialName("shareUser") val shareUser: String?,

    @SerialName("chapterName") val chapterName: String,

    @SerialName("link") val link: String? = "",

    @SerialName("niceDate") val niceDate: String = "",
    @SerialName("collect") val isCollect: Boolean = false,
)