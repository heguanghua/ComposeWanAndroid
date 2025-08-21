package com.compose.wan.android.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SingleWeb(
    @SerialName("id") val id: Int,

    @SerialName("name") val name: String,

    @SerialName("link") val link: String,

    @SerialName("visible") val visible: Int,
)