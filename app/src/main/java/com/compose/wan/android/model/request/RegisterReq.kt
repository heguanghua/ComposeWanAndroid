package com.compose.wan.android.model.request

import kotlinx.serialization.Serializable

/// username,password,repassword
@Serializable
data class RegisterReq(
    val username: String,
    val password: String,
    val repassword: String
)