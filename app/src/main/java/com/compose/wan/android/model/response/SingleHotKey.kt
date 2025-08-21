package com.compose.wan.android.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SingleHotKey(
    @SerialName("id") val id: Int,

    @SerialName("name") val name: String,

    @SerialName("visible") val visible: Int,
)