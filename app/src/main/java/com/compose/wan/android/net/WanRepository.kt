package com.compose.wan.android.net

import com.compose.wan.android.model.response.HomeArticleListResp
import com.compose.wan.android.model.response.SingleHomeBanner
import com.compose.wan.android.model.response.SingleHotKey
import com.compose.wan.android.model.response.SingleWeb
import javax.inject.Inject

class WanRepository @Inject constructor() {
    suspend fun getHomeArticles(page: Int): HomeArticleListResp {
        return WanService.instance.getHomeArticles(page)
    }

    suspend fun getHomeBanners(): List<SingleHomeBanner> {
        return WanService.instance.getHomeBanners()
    }

    suspend fun getHotKeys(): List<SingleHotKey> {
        return WanService.instance.getHotKeys()
    }

    suspend fun getHotWebs(): List<SingleWeb> {
        return WanService.instance.getHotWebs()
    }

    suspend fun register(username: String, password: String, repassword: String) {
        return WanService.instance.register(username, password, repassword)
    }
}