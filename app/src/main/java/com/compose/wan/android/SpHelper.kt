package com.compose.wan.android

import androidx.core.content.edit
import com.compose.wan.android.constant.SpKey

object SpHelper {
    private val sp by lazy {
        WanApplication.instance.getSharedPreferences("sp", 0)
    }

    fun putString(key: String, value: String) {
        sp.edit { putString(key, value) }
    }

    fun getString(key: String): String? {
        return sp.getString(key, null)
    }

    fun putStringList(key: String, value: List<String>) {
        sp.edit { putString(key, value.joinToString(",")) }
    }

    fun getStringList(key: String): List<String> {
        return sp.getString(key, null)?.split(",") ?: emptyList()
    }

    fun clearUserInfo() {
        sp.edit {
            remove(SpKey.USER_NAME)
        }
    }
}