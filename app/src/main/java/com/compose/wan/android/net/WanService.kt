package com.compose.wan.android.net

import com.compose.wan.android.constant.ApiConstant
import com.compose.wan.android.model.response.HomeArticleListResp
import com.compose.wan.android.model.response.SingleHomeBanner
import com.compose.wan.android.model.response.SingleHotKey
import com.compose.wan.android.model.response.SingleWeb
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
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
                .addConverterFactory(
                    json.asConverterFactory("application/json".toMediaType())
                )
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

    @GET(ApiConstant.HOT_KEY)
    suspend fun getHotKeys(): List<SingleHotKey>

    @GET(ApiConstant.FRIEND)
    suspend fun getHotWebs(): List<SingleWeb>

    @FormUrlEncoded
    @POST(ApiConstant.USER_REGISTER)
    suspend fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("repassword") repassword: String
    )
}