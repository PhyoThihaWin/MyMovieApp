package com.pthw.mymovieapp.di

import android.content.Context
import com.pthw.mymovieapp.BuildConfig
import com.pthw.mymovieapp.utils.ApiKeyInterceptor
import com.readystatesoftware.chuck.ChuckInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit


fun createRetrofitClient(url: String, okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder().baseUrl(url)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
}

fun createOkHttpClient(context: Context): OkHttpClient {
    val builder = OkHttpClient.Builder()
    builder.connectTimeout(60, TimeUnit.SECONDS)
    builder.writeTimeout(60, TimeUnit.SECONDS)
    builder.readTimeout(60, TimeUnit.SECONDS)

    if (BuildConfig.DEBUG) {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        builder.networkInterceptors().add(httpLoggingInterceptor)
        builder.addInterceptor(ApiKeyInterceptor())
        builder.addInterceptor(ChuckInterceptor(context))

//        builder.addInterceptor(tokenInterceptor)
    }else{
        builder.addInterceptor(ApiKeyInterceptor())
//        builder.addInterceptor(tokenInterceptor)
    }

    return builder.build()
}