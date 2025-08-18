package com.compose.wan.android.net

import android.widget.Toast
import com.compose.wan.android.WanApplication
import com.compose.wan.android.constant.ErrorCodeConstant
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.serializer
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type
import kotlinx.serialization.json.Json as KotlinJson

class DataConverterFactory(private val json: KotlinJson) : Converter.Factory() {

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {

        // 创建目标类型的序列化器
        val dataSerializer = json.serializersModule.serializer(type)

        return Converter<ResponseBody, Any?> { body ->
            try {
                // 解析为 JSON 对象
                val jsonElement = json.parseToJsonElement(body.string())
                val jsonObject = jsonElement.jsonObject

                // 获取基础字段
                val code = jsonObject["errorCode"]?.toString()?.toIntOrNull() ?: -1
                val message = jsonObject["errorMsg"]?.toString().orEmpty()
                val data = jsonObject["data"]

                // 检查错误码
                if (code == ErrorCodeConstant.SUCCESS) {
                    // 成功则解析 data 字段
                    if (data == null) {
                        throw NullPointerException("Response data is null")
                    }

                    // 将 data 部分转换为目标类型
                    json.decodeFromJsonElement(dataSerializer, data)
                } else {
                    // 业务错误抛出异常
                    throw ApiException(code, message).also {
                        handleApiError(code, message)
                    }
                }
            } catch (e: Exception) {
                // 处理解析错误
                throw when (e) {
                    is ApiException -> e
                    else -> RuntimeException("JSON parsing error: ${e.message}", e)
                }
            }
        }
    }

    private fun handleApiError(code: Int, message: String) {
        when (code) {
            else -> MainScope().launch {
                Toast.makeText(WanApplication.instance, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        fun create(json: KotlinJson): DataConverterFactory {
            return DataConverterFactory(json)
        }
    }
}