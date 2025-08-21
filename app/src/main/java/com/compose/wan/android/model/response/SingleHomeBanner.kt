package com.compose.wan.android.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SingleHomeBanner (
    @SerialName("imagePath") val imagePath: String,

    @SerialName("url") val url: String,

    @SerialName("desc") val desc: String,
)