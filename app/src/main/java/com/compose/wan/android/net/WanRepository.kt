package com.compose.wan.android.net

import com.compose.wan.android.model.response.HomeArticleListResp
import javax.inject.Inject

class WanRepository @Inject constructor() {
    suspend fun getHomeArticles(page: Int): HomeArticleListResp {
        return WanService.instance.getHomeArticles(page)
    }
}