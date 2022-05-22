package com.pthw.mymovieapp.utils

import com.pthw.mymovieapp.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class ApiKeyInterceptor  :
    Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        val original = chain.request()
        val originalHttpUrl = original.url.newBuilder().addQueryParameter("api_key", BuildConfig.API_KEY).build()
        val request = original.newBuilder().url(originalHttpUrl).build()

        return chain.proceed(request)
    }
}
