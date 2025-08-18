package com.compose.wan.android.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SingleHomeArticle (
    @SerialName("author") val author: String = "",

    @SerialName("shareUser") val shareUser: String = "",

    @SerialName("link") val link: String = "",

    @SerialName("niceDate") val niceDate: String = "",
)