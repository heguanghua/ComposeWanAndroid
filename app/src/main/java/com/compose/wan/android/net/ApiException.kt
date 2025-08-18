package com.compose.wan.android.net

class ApiException(val errorCode: Int, message: String) : Exception(message)