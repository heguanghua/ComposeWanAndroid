package com.compose.wan.android.net

import com.compose.wan.android.model.response.HomeArticleListResp
import com.compose.wan.android.model.response.SingleHomeBanner
import javax.inject.Inject

class WanRepository @Inject constructor() {
    suspend fun getHomeArticles(page: Int): HomeArticleListResp {
        return WanService.instance.getHomeArticles(page)
    }

    suspend fun getHomeBanners(): List<SingleHomeBanner> {
        return WanService.instance.getHomeBanners()
    }
}