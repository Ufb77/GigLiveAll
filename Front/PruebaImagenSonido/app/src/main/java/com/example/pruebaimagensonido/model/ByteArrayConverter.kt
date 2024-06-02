package com.example.pruebaimagensonido.model
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class ByteArrayConverter : Converter<ResponseBody, ByteArray> {
    override fun convert(value: ResponseBody): ByteArray? {
        return value.bytes()
    }
}

class ByteArrayConverterFactory : Converter.Factory() {
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        return if (type == ByteArray::class.java) {
            ByteArrayConverter()
        } else {
            null
        }
    }
}