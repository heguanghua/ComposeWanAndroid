package com.compose.wan.android.net

import okhttp3.Interceptor
import okhttp3.Response

class CookieInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse = chain.proceed(chain.request())

        val cookies = originalResponse.headers["set-cookie"]
        if (cookies != null) {
            // 将 Cookie 添加到请求头中
            val requestBuilder = originalResponse.request.newBuilder()
            requestBuilder.addHeader("Cookie", cookies)
            return chain.proceed(requestBuilder.build())
        }
        return originalResponse
    }
}