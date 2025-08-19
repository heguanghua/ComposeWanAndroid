package com.compose.wan.android.net

import com.compose.wan.android.constant.ApiConstant
import com.compose.wan.android.model.response.HomeArticleListResp
import com.compose.wan.android.model.response.SingleHomeBanner
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.concurrent.TimeUnit

interface WanService {
    companion object {
        val instance by lazy { create() }
        private fun create(): WanService {
            val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(25, TimeUnit.SECONDS)
                .readTimeout(25, TimeUnit.SECONDS)
                .writeTimeout(25, TimeUnit.SECONDS)
                .callTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(HttpLoggingInterceptor { message ->
                    println(message)
                }.setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()

            val json = Json {
                ignoreUnknownKeys = true // 忽略未知字段
                coerceInputValues = true
                isLenient = true
                encodeDefaults = true
            }

            val retrofit = Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(ApiConstant.BASE_URL)
                .addConverterFactory(DataConverterFactory.create(json))
                .build()

            return retrofit.create(WanService::class.java)
        }
    }

    @GET(ApiConstant.HOME_ARTICLE_LIST)
    suspend fun getHomeArticles(
        @Path("page") page: Int
    ): HomeArticleListResp

    @GET(ApiConstant.HOME_BANNER)
    suspend fun getHomeBanners(): List<SingleHomeBanner>
}